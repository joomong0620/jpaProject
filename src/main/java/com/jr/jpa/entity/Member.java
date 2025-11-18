package com.jr.jpa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.jr.jpa.enums.CommonEnums;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entity (엔티티) : @Entity가 붙은 실제 클래스 또는 그 인스턴스 또는 JPA가 관리하는 객체
// 					DB 테이블의 한 행과 엔티티 객체가 1:1로 매핑
// # 사원: Entity , 사원증: @Entity
@Entity  //@Entity 어노테이션 : JPA를 사용해 테이블과 매핑할 클래스에 붙여주는 어노테이션

@Builder // 복잡한 객체 생성을 쉽게 하기 위해 사용하는 어노테이션 
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor



@DynamicInsert //INSERT 시 null이 아닌 필드(== 작성한 값)만 SQL 에 포함
			   // 나머지는 default값 활용
@DynamicUpdate // UPDATE 시 변경된 필드만 SQL에 포함
				// 하나로 묶어서 DTO에서 관리하고 있다.
public class Member {
	// @Id : 기본키(PK) 지정
	// @Column: 컬럼 이름, 길이, null 허용 여부 설정

	@Id
	@Column(name="MEMBER_ID", length = 30)
	private String memberId;
	
	@Column(name="MEMBER_PW", length = 100, nullable = false)
	// name : "MEMBER_PW" : DB 컬럼 이름을 MEMBER_PW로 지정함
	// length = 100 : 문자열 길이를 최대 100 자로 제한함 (varchar(100), 미작성 시 255 기본값)
	// nullable = false : NULL 값 허용 X (NOT NULL 제약조건 생성)
	
	private String memberPw;
	
	@Column(name="MEMBER_NAME", length=15, nullable=false)
	private String memberName;
	
	@Column(length=254)
	private String email; 
	
	@Column(length = 1, nullable=false)
	@Enumerated(EnumType.STRING)
	// - JPA에서 enum 타입 필드를 DB에 어떻게 저장할지 지정하는 어노테이션
	// 숫자가 아닌 문자열 (enum 이름)으로 저장함
	private CommonEnums.Status status; 
	
	// @Column 없으면 기본 설정
	// - 컬럼 이름 : age, nullable=true
	// int -> 0 , 객체 -> null
	// - null과 실제 값의 차이를 구분하기 위해서 Integer 사용
	private Integer age;
	
	private String address;
	
	@Column(length = 13)
	private String phone;
	
	
	@Column(length = 1)
	@Enumerated(EnumType.STRING) //# Enum 타입을 어떻게 지정할지
	private Gender gender;
	
	public enum Gender{
		F, M
	}
	
	@Column(name="ENLOLL_DATE")
	private LocalDateTime enrollDate;
	
	@Column(name="MODIFY_DATE")
	private LocalDateTime modifyDate;
	
	@PrePersist
	// @PrePersist : JPA가 INSERT 하기 직전에 자동으로 실행되는 메소드
	public void prePersist() {
		
		this.enrollDate = LocalDateTime.now();
		this.modifyDate = LocalDateTime.now();
		if(this.status == null) {
			this.status = CommonEnums.Status.Y; 
		}
		// 디폴트 값 지정해주는 것
	}
	// 회원 정보 수정 메소드
	public void updateMemberInfo(String memberName, String email, Integer age,
			String address, String phone, Gender gender) {
		this.memberName = memberName;
		this.email = email;
		this.age = age;
		this.address= address;
		this.phone = phone;
		this.gender = gender;
	
	
	}
	
	// Member : Board(1:N)
	@OneToMany(mappedBy="member", cascade=CascadeType.ALL)
		// mappedBy = "연관관계 주인의 필드명"
		// casecade = CasecadeType.ALL : 부모(Nember)에서 자식(Board)으로 cascade 전파
		// ALL: 저장, 수정, 삭제, refresh, detach 즉, 부모에서 하는 영속성 관련 동작을 자식에게 모두 전파
		private List<Board> boards = new ArrayList<>();
	
	
		// 연관관계 편의 메소드
		public void addBoard(Board board) {
			this.boards.add(board); // List에 추가 # 여기서 this 쓰나 안쓰나 똑같음
			board.setMember(this); // 반대편(연관관계 주인)도 같이 세팅 # this는 항상 나를 가리킴
			
		
		}
	
	
	
	
	

	
}
