package com.jr.jpa.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jr.jpa.dto.BoardDto.Create;
import com.jr.jpa.entity.Board;
import com.jr.jpa.entity.BoardTag;
import com.jr.jpa.entity.Member;
import com.jr.jpa.entity.Tag;
import com.jr.jpa.repository.BoardRepository;
import com.jr.jpa.repository.MemberRepository2;
import com.jr.jpa.repository.TagRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // readOnly가 아니라면 불필요하게 실행하게 되니까! 필요할 때만 사용한다. 
public class BoardServiceImpl implements BoardService {
   
   private final BoardRepository boardRepository;
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

}
