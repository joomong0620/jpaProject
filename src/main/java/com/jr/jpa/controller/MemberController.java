package com.jr.jpa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jr.jpa.dto.MemberDTO;
import com.jr.jpa.service.MemberService;

import lombok.RequiredArgsConstructor;


@RestController //@Controller + @ResponseBody 
@RequestMapping("/api/members") // get, post 둘다 가능
@RequiredArgsConstructor // final로 선언된 필드를 자동으로 생성자의 매개변수로 넣어줌
						// -> final이 없는 필드는 자동으로 주입 X
						// autowired 없어도 됨

public class MemberController {
	
	private final MemberService memberService; // 한 번만 세팅 가능 : 불변성 보장

	/*
	// 매번 작성하기 번거롭다.. -> 해결 방안 : @RequiredArgsConstructor 사용
	//@Autowired (Spring 4.3 이상 부터 생략 가능)
	public MemberController(MemberSerivce memberService, BoardService boardService){
		this.memberService = memberService;
		this.boardService = boardService;
	}
	*/
	
	// 회원 등록 API
	@PostMapping
	public ResponseEntity<String> addMember(@RequestBody MemberDTO.Create createDto){
		// ResponseEntity : HTTP 상태 코드, 헤더, 본문을 포함해 응답을 세밀하게 제어할 때 쓰는 클래스
		
		
		// ResponseEntity<String> : 문자열(JSON이 아닌 순수 문자열)로 응답
		
		String memberId = memberService.createMember(createDto);
		// return new ResponseEntity<String>(memberId, HttpStatus.OK);
		return ResponseEntity.ok(memberId);
		// ResponseEntity.ok(응답 데이터)
		// - HTTP 상태 코드 200(성공)과 함께 본문 (body)에 저장된 회원의 memberId를 담아서 응답한다.

	}
	
	// 아디이가 일치하는 회원 조회 == 한 명만 조회하겠다.
	@GetMapping("/{memberId}")
	public ResponseEntity<MemberDTO.Response> findById(@PathVariable /*("memberId")*/ String memberId ){
		return ResponseEntity.ok(memberService.findById(memberId));
	}
	
	// 모든 회원 조회 getAllMembers랑 findAllMember랑 같지 않아도 상관 없다.
	@GetMapping
	public ResponseEntity<List<MemberDTO.Response>> getAllMembers(){
		return ResponseEntity.ok(memberService.findAllMember());
	}
	
	
	// 회원 정보 수정
	@PutMapping("/{memberId}")
	public ResponseEntity<MemberDTO.Response> updateMember(
			@PathVariable String memberId,
			@RequestBody MemberDTO.Update updateDto){
		
		return ResponseEntity.ok(memberService.updateMember(memberId, updateDto));
	}
	
	// 회원 삭제
	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> deletMember(@PathVariable String memberId){
		// ResponseEntity : HTTP 요청에 대한 응답을 생성하고 제어하는데 사용하는 클래스
		// ResponseEntity<Void> :> HTTP에 응답은 보내지만, body는 없다.
		//(== 이 응답에는 본문이 없다)
		
		memberService.deleteMember(memberId);
		return ResponseEntity.ok().build();
		
		//HTTP 응답을 반환
		//ok(): 상태코드 200(성공)
		//build() : 응답 바디 없이 빈 응답 생성
	

		
	}
	
	// 이름으로 회원 검색
	@GetMapping("/search/name")
	 public ResponseEntity<List<MemberDTO.Response>> searchMemberByName(
			 @RequestParam String name){
		
		return ResponseEntity.ok(memberService.findByMemberName(name));
	}
	
	
	
	
	
}
