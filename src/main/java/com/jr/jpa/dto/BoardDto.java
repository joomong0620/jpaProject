package com.jr.jpa.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jr.jpa.entity.Board;

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
}
