package com.jr.jpa.enums;


public class CommonEnums {
		// enum : 정해진 값 중 하나만 쓸 수 있게 제한하는 타입
		// - 오타나 잘못된 값 사용 시 컴파일 에러 발생 -> 실수 방지
		// - 성별, 상태, 역할처럼 값이 정해져있는 경우 사용
		public enum Status{
			Y, N 
		}
}
