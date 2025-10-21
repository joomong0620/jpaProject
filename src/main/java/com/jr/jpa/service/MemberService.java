package com.jr.jpa.service;

import java.util.List;

import com.jr.jpa.dto.MemberDTO;
import com.jr.jpa.dto.MemberDTO.Create;
import com.jr.jpa.dto.MemberDTO.Update;

public interface MemberService {

	
	// 회원 등록
	String createMember(Create createDto);

	// 아이디가 일치하는 회원 조회
	MemberDTO.Response findById(String memberId);

	// 회원 목록 조회
	List<MemberDTO.Response> findAllMember();

	// 회원 정보 수정 
	MemberDTO.Response updateMember(String memberId, Update updateDto);

	// 회원 삭제 
	void deleteMember(String memberId);
	
	// 이름이 일치하는 회원 목록 조회
	List<MemberDTO.Response> findByMemberName(String name);

	
	
	
	

}
