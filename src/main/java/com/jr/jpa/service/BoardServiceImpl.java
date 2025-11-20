package com.jr.jpa.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jr.jpa.dto.BoardDto;
import com.jr.jpa.dto.BoardDto.Create;
import com.jr.jpa.dto.BoardDto.Response;
import com.jr.jpa.dto.BoardDto.Update;
import com.jr.jpa.entity.Board;
import com.jr.jpa.entity.BoardTag;
import com.jr.jpa.entity.Member;
import com.jr.jpa.entity.Tag;
import com.jr.jpa.enums.CommonEnums;
import com.jr.jpa.repository.BoardRepository2;
import com.jr.jpa.repository.MemberRepository2;
import com.jr.jpa.repository.TagRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // readOnly가 아니라면 불필요하게 실행하게 되니까! 필요할 때만 사용한다. 
public class BoardServiceImpl implements BoardService {

//	private final BoardRepository boardRepository;
	private final BoardRepository2 boardRepository; // 왜 갈아끼워요>??
	private final MemberRepository2 memberRepository;
	private final TagRepository tagRepository;
	

	private final String UPLOAD_PATH = "C:\\workspace\\6_SpringBoot\\jpaProject\\src\\main\\resources\\static\\images\\board\\";

	// 게시글 작성
	@Override
	@Transactional // 기본값 readOnly = false
	public Long createBoard(Create boardCreate) throws IllegalStateException, IOException {
		// 작성자 찾기 -> 객체지향코드를 작성할 것이기 때문에 key를 직접 외래키로 insert하지 않고
		// 작성자를 찾아서 참조해준다.

		Member member = memberRepository.findById(boardCreate.getMemberId())
				.orElseThrow(()-> new EntityNotFoundException("일치하는 회원이 없습니다."));

		Board board = boardCreate.toEntity();


		//      member.addBoard(board); //Member의 연관편의 메소드
		board.addMember(member); // Board의 연관편의 메소드
		//무슨차이가 있냐? 회원이 게시글을 관리한다면 멤버 쪽에다가 편의를 작성할 수 있고 반대도 될 수 있다
		// 카테고리를 수정한다던가 할 수 있다. 정답 없음. 의미만 좀 달라질 뿐
		// 어디서 관리하느냐에 초점을 둘 것	


		// 태그는 null일 수도 있고 빈 리스트일 수도 있으므로 둘다 검사해야함
		if(boardCreate.getTags() != null && !boardCreate.getTags().isEmpty()) {
			// 태그가 존재하는 경우["java", "jpa", "DB"]
			for(String tagName: boardCreate.getTags()) {

				// tagName으로 조회해서 없는 경우 새로 만들기

				Tag tag = tagRepository.findByTagName(tagName)
						// orElseGet(): 태그의 값이 없는 경우 태그를 저장
						.orElseGet(()-> tagRepository.save(Tag.builder()
								.tagName(tagName)
								.build()));

				// BoardTag에 추가하기
				BoardTag boardTag = BoardTag.builder()
						.tag(tag)
						.build();
				boardTag.addBoard(board); // 연관편의 메소드 호출


			}
		}

		// 이미지 
		String changeName = null;
		String originName = null;

		if(boardCreate.getFile() != null && !boardCreate.getFile().isEmpty()) {
			originName = boardCreate.getFile().getOriginalFilename();
			changeName = UUID.randomUUID().toString() + "_" + originName;
			// UUID(Universally Unique Identifier)
			// UUID.randomUUID() : 전 세계에서 거의 겹치지 않는 128비트 식별자(UUID)를
			//						랜덤하게 만들어주는 메소드


			// 업로드할 폴더를 파일객체로 생성 
			File uploadDir = new File(UPLOAD_PATH);
			// 해당 경로가 없는 경우 생성하기
			if(!uploadDir.exists()) uploadDir.mkdirs();

			boardCreate.getFile().transferTo(new File(UPLOAD_PATH + changeName));

		}

		board.changeFile(originName, changeName);

		return boardRepository.save(board).getBoardNo();





	}

	// 게시글 목록 조회
	@Override
	public Page<Response> getBoardList(Pageable pageble) {


		Page<Board> page = boardRepository.findByStatus(CommonEnums.Status.Y, pageble);


		/* Page에는 Board 목록, 전체 페이지 수, 현재 페이지 번호, ... 등 관련된 페이징 정보가 모두 담겨 있다.
		 * getContent() : 실제 데이터 리스트
		 * getTotalPages() : 전체 페이지 수
		 * getTotalelements() : 전체 데이터 수
		 * ...
		 * */



		// 변환하는 용도 : map
		// Board 엔티티 -> Board DTO로 변환
		return page.map(Response::toSimpleDto);
	}


	// 게시글 상세 조회
	@Override
	public Response getBoardDetail(Long boardNo) {
		Board board = boardRepository.findById(boardNo)
				.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

		return Response.toDto(board);
	}




	// 게시글 수정
	@Transactional
	@Override
	public Response updateBoard(Long boardNo, Update updateBoard) throws IllegalStateException, IOException {

		// 해당 게시글이 존재하는지 조회
		Board board = boardRepository.findById(boardNo)
				.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));


		// 제목 변경
		board.changeTitle(updateBoard.getBoardTitle());

		// 내용 변경
		board.changeContent(updateBoard.getBoardContent());



		// 태그가 있는 경우
		// 기존 태그 삭제 후 저장
		if(updateBoard.getTags() != null && !updateBoard.getTags().isEmpty()) {

			// 기존 BoardTag 연결 끊기
			// -> 더이상 필요하지 않으므로 연결된 boardTag의 영속성 제거 
			// -> orphanRemoval=True로 설정되어있는 경우 실제 DB에서 제거
			board.getBoardTags().clear();

			for(String tagName: updateBoard.getTags()) {
				Tag tag = tagRepository.findByTagName(tagName)
						.orElseGet(()-> tagRepository.save(Tag.builder()
								.tagName(tagName)
								.build()));
				BoardTag boardTag = BoardTag.builder().tag(tag).build();

				boardTag.addBoard(board);
			}

		}	


		// 이미지가 존재하는 경우 전달받은 이미지로 변경
		String originName = board.getOriginName();
		String changeName = board.getChangeName();


		if(updateBoard.getFile() != null && !updateBoard.getFile().isEmpty()) {
			originName = updateBoard.getFile().getOriginalFilename();
			changeName = UUID.randomUUID().toString() + "_" + originName;
			// 업로드할 폴더를 파일객체로 생성
			File uploadDir = new File(UPLOAD_PATH);

			// 해당 경로가 없는 경우 생성하기
			if(!uploadDir.exists()) uploadDir.mkdirs();

			updateBoard.getFile().transferTo(new File(UPLOAD_PATH + changeName));
		}

		board.changeFile(originName, changeName);


		// DTO로 변환해서 보내기
		return BoardDto.Response.toDto(board);

	}

	// 게시글 삭제
	@Override
	@Transactional
	public void updateStatus(Long boardNo) {
		// 해당 게시글이 존재하는지 조회
		Board board = boardRepository.findById(boardNo)
				.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
		

		// # 해당 경로에 업로드된 파일이 있다면 찾아서 그 파일을 삭제 하겠다.
		
		
		// 방법 1) 상태 변경 메소드
		board.setStatus(CommonEnums.Status.N);   // 삭제 상태로 변경
		
		
//		// 방법 2) 삭제
//		boardRepository.delete(board);

	}
	
	@Transactional 
	@Override
	public void deleteBoard(Long boardNo) {

	// 해당 게시글이 존재하는지 조회
			Board board = boardRepository.findById(boardNo)
					.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
			
			
			// 업로드된 파일이 있다면 UPLOAD_PATH 경로의 파일 삭제
			if(board.getChangeName() != null) {
				new File(UPLOAD_PATH + board.getChangeName()).delete();
			}
			
			// 방법 2) 삭제
			boardRepository.delete(board);
	}


}

