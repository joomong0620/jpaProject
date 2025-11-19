package com.jr.jpa.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jr.jpa.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class BoardDto {
	
	// 게시글 생성 요청을 받았을 때 사용할 DTO
	@Getter
	@Setter
	@ToString
	public static class Create{ // static이 붙으면 제일 먼저 로드
		private String boardTitle;
		private String boardContent;
		private String memberId; 
		private MultipartFile File;
		private List<String> tags;
		
		
		// 주의사항: @Builder을 이용하여 객체를 생성하는 경우
		// 			아무 값도 안 넣으면 필드에 초기화된 값을 무시하고 null로 둔다.
		// 해결방법 : @Builder.Default 사용
		public Board toEntity() {
			return Board.builder()
					.boardTitle(this.boardTitle)
					.boardContent(this.boardContent)
					.build();
		}
	}
	
	@Builder
	@Getter
	@ToString
	public static class Response{
		private Long boardNo;
		private String boardTitle;
		private String boardContent;
		private String originName;
		private String changeName;
		private LocalDateTime createDate;
		private Integer count;
		private String memberId;
		private String memberName;
		private List<String> tags;
		
		
		
		// 게시글 상세 조회용 DTO
		public static Response toDto(Board board) {
			return Response.builder()
		            .boardNo(board.getBoardNo())
		            .boardTitle(board.getBoardTitle())
		            .boardContent(board.getBoardContent())
		            .originName(board.getOriginName())
		            .count(board.getCount())
		            .changeName(board.getChangeName())
		            .createDate(board.getCreateDate())
		            .memberId(board.getMember().getMemberId())
		            .memberName(board.getMember().getMemberName())
		            .tags(board.getBoardTags() // 태그 넘버 필요 없고 그냥 태그명만 필요하다. 반복문 사용
		            		.stream()
		            		.map(boardTag -> boardTag.getTag().getTagName())
		            		.toList())
		            .build();
		}
		
		
		// 게시글 목록조회용 DTO
		public static Response toSimpleDto(Board board) {
			// 전달받은 Entity를 DTO로 변환
			// -> Builder 패턴 만들고자 할 때는 Builder 어노테이션 필수
			return Response.builder()
					.boardNo(board.getBoardNo())
					.boardTitle(board.getBoardTitle())
					.originName(board.getOriginName())
					.count(board.getCount())
					.createDate(board.getCreateDate())
					.memberId(board.getMember().getMemberId())
					.build();
			
			// 게시글 번호
			// 게시글 제목
			// 파일 원본명
			// 조회수
			// 생성날짜
			// 회원아이디
		}
		

	}
	// 게시글 수정용 DTO
	@Getter
	@ToString
	@Setter
	public static class Update {
		private String boardTitle;
		private String boardContent;
		private MultipartFile File;
		private List<String> tags;
		
//		public Board toEntity() {
//			return Board.builder()
//					.boardTitle(this.boardTitle)
//					.boardContent(this.boardContent)
//					.build();
//		} => 없어도 됨
		
	}

}
	
	
	
	
