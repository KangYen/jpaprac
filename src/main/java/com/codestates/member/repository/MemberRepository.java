package com.codestates.member.repository;

import com.codestates.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    // 조회할 멤버가 db에 저장되있는 멤버의 이메일과 알치하는지
    // 검증하기 위해서 Repository에  이 코드가 있음
    // isPresent()로 만약 존재할 경우 MEMBER_EXIST같은 예외를 던지게 설계
}

//이름이나 폰은 사용자의 요청에따라 수정이 될 여지가 있는 반면에
//이메일은 숫자,특수문자가 들어가는 고유한 주소라서
//중복의 여지가 그나마 없는 이메일로 검증하는 것
