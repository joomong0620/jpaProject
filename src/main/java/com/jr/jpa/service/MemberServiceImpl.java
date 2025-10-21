package com.jr.jpa.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jr.jpa.dto.MemberDTO;
import com.jr.jpa.dto.MemberDTO.Create;
import com.jr.jpa.dto.MemberDTO.Response;
import com.jr.jpa.dto.MemberDTO.Update;
import com.jr.jpa.entity.Member;
import com.jr.jpa.respository.MemberRepository2;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


	//private final MemberRepository memberRepository;
	private final MemberRepository2 memberRepository; // #수정과 유지보수에 용이
													//# 한 줄만 바꿨는데 더 좋아진 코드
	

	

	@Override
	public String createMember(Create createDto) {
		
	// createDto : 사용자가 입력한 회원 정보
	// toEntity() : DTO를 실제 DB에 저장할 수 있는 엔티티 객체로 변환하는 메소드
	Member member = createDto.toEntity(); // 객체 생성(비영속 상태)
		
		memberRepository.save(member);
		
		
		return member.getMemberId();
	}


	// 아이디가 일치하는 회원 한명 조회
	// DB에서 읽기만 하므로 트랜잭션을 읽기 전용(readOnly)으로 설정
	// :: = 참조한다.
	@Transactional(readOnly = true)
	@Override
	public MemberDTO.Response findById(String memberId) {
		return memberRepository.findById(memberId) // 1차 캐시에 저장됨(영속 상태)
				.map(MemberDTO.Response::toDto) 
				// 만약 Member 객체가 존재한다면, 
				//MembeDTO.Response.toDto(member)로 호출해서 DTO로 반환
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
				// 존재하지 않는 경우 메세지와 함께 IllegalArgumentException 발생 
	}


	// 회원 목록 조회
	@Override
	public List<Response> findAllMember() {
		return memberRepository.findAll().stream()
				// stream() : 가져온 리스트를 Java Stream API로 변환
				//	-> 데이터를 순차적으로 처리 가능
				// For문 보다 더 간단하게 처리 가능하다.
				.map(MemberDTO.Response::toDto)
				// Member 객체가 존재하면 각 Member 객체를 MemberDTO.Response로 변환
				
				.collect(Collectors.toList());
				// 변환된 MemberDTO.Response 객체들을 다시 리스트 형태로 수집
				// -> List<MemberDTO.Response>로 변환됨.
		
		
		
	}

	   // 회원 정보 수정
	   @Override
	   public Response updateMember(String memberId, Update updateDto) {
	      // 1. 해당 아이디를 가진 회원을 조회
	      Member member = memberRepository.findById(memberId) // 1차 캐시에 저장(영속 상태)
	            .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 회원입니다.") );
	      
	      // 2. 존재한다면 회원 정보 수정
	      member.updateMemberInfo(
	            updateDto.getMember_name(), 
	            updateDto.getEmail(), 
	            updateDto.getAge(), 
	            updateDto.getAddress(),
	            updateDto.getPhone(),
	            updateDto.getGender());
	      
	      // 엔티티 자체를 넘기면 보안 문제! Entity -> DTO로 변환하여 반환함
	      return MemberDTO.Response.toDto(member);
	   }


	   //회원 삭제
	   @Override
	   public void deleteMember(String memberId) {
		   // 1) 해당 아이디를 가진 회원 조회
		   Member member = memberRepository.findById(memberId)
			   				// 1차 캐시에 저장된(영속 상태) + 반환 타입 Optional 
				   			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		   //2) 존재한다면 회원 삭제
		   memberRepository.delete(member);
			//#1차 캐시에 등록을 안 시켜두면 엔티티 메니저가 누군데?라고 판단.
		   	//
	}


	 // 이름이 일치하는 회원 목록 조회
	   @Override
	   public List<Response> findByMemberName(String name) {
		   return memberRepository.findByMemberNameContainingOrderByAgeDesc(name)
				   // -> DB에서 전달받은 name과 일치하는 회원의 목록 가져옴<List<Member>반환 받음)
				   
				   // findByMemberName(name) 호출하는 경우 :
				   // -> 전달한 name과 완벽히 일치하는 결과
				   // findByMemberNameContainingOrderByAgeDesc(name)
				   // -> 전달한 name을 포함한 결과를 age 내림차순으로 정렬
				   
				   .stream()
				   // stream() : 가져온 회원 목록을 스트림 형태로 변환 (반복 처리를 쉽게하기 위해서)
				   
				   .map(MemberDTO.Response::toDto)
				   //map() : 각 Member 엔티티 객체를 MemberDTO.Response 객체로 변환
				   // 		-> toDto라는 메소드 이용
				   
				   .collect(Collectors.toList());
		   			// .collect(Collectors.toList()) : 변환된 DTO들을 다시 리스트 형태로 모음
		   			// 최종적으로 List<MemberDTO.Response> 반환
	   }

}
