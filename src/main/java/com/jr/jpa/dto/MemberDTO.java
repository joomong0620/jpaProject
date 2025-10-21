package com.jr.jpa.dto;

import java.time.LocalDateTime;

import com.jr.jpa.entity.Member;
import com.jr.jpa.enums.CommonEnums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDTO {
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	// 내부 클래스(Inner Class) : 클래스(MemberDTP) 안에 정의된 또 다른 클래스
	// 정적 내부 클래스(Static Inner Class) : 독립적으로 사용될 클래스
	public static class Create{
		private String member_id;
		private String member_pw;
		private String member_name;
		private String email;
		private Integer age;
		private String address; 
		private String phone;
		private Member.Gender gender;
		
		// 빌더 패턴(Builder Pattern)
		
		// 복잡한 객체를 단계적으로 생성할 수 있도록 도와주는 디자인 패턴
		// - 생성자나 setter로 초기화하는 대신, 가독성있고 안전하게 객체를 만들 수 있다.
		// - 값을 안넣으면 null 값이 들어감 .
		
		
		// toEntity: DTO를 실제 DB에 저장할 수 있는 엔티티 객체로 변환하는 메소드
		public Member toEntity() {
			return Member.builder()
						 .memberId(this.member_id)
						 .memberPw(member_pw)
						 .memberName(this.member_name)
						 .email(this.email)
						 .address(this.address)
						 .phone(this.phone)
						 .gender(this.gender)
						 .age(this.age)
						 .build();
						 
		}	
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	@Builder // 객체를 만들 때 빌더 패턴으로 자동으로 만들어주는 어노테이션
	public static class Response{
		private String member_id;
		private String member_name;
		private String email;
		private Integer age;
		private String address; 
		private String phone;
		private Member.Gender gender;
		private LocalDateTime modifyDate;
		private LocalDateTime enrollDate;
		private CommonEnums.Status status;
		
		
		//Entity -> DTO로 변환
		// 엔티티 전체를 직접 반환하면 보완 위험 때문에 필요한 정보만 골라서 반환해야 한다.
		public static Response toDto(Member member) {
			return Response.builder()
					 .member_id(member.getMemberId())
					 .member_name(member.getMemberName())
					 .email(member.getEmail())
					 .address(member.getAddress())
					 .phone(member.getPhone())
					 .gender(member.getGender())
					 .age(member.getAge())
					 .modifyDate(member.getModifyDate())
					 .status(member.getStatus())
					 .build();
			
			
		}
	}
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class Update{
		private String member_name;
		private String email;
		private Integer age;
		private String address; 
		private String phone;
		private Member.Gender gender;
	}
}
