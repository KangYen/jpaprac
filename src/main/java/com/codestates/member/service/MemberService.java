package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.utils.CustomBeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<Member> beanUtils;    // -> 리팩토링 위한 것
    // 제네릭 적용 클래스라서 < > 안에 어떤 엔티티의 Service 클래스에 적용할 건지 써줘야함

    public MemberService(MemberRepository memberRepository, CustomBeanUtils<Member> beanUtils) {
        this.memberRepository = memberRepository;
        this.beanUtils = beanUtils;
    }
    public Member createMember(Member member) {
        // 이미 등록된 이메일인지 확인
        verifyExsistsEmail(member.getEmail());

        return  memberRepository.save(member);
    }
    // 리팩토링 전
//    public Member updateMemberOld(Member member) {
//        Member findMember = findVerifiedMember(member.getMemberId());
//
//        //수정할 정보들이 늘어나면 반복되는 코드가 늘어나는 문제점이 있음
//        Optional.ofNullable(member.getName())
//                .ifPresent(name -> findMember.setName(name));
//        Optional.ofNullable(member.getPhone())
//                .ifPresent(phone -> findMember.setPhone(phone));
//        // 추가된 부분
//        Optional.ofNullable(member.getMemberStatus())
//                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));
//        // findMember.setModifiedAt(LocalDateTime.now());
//
//        return memberRepository.save(findMember);
//    }

    //리팩토링 후
    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId());
        // 아래 구현한 findVerifiedMember() 메서드에 Member 클래스의 Id룰 Getter 로 가져와서 인자로  넣고 존재하는지 확인
        beanUtils.copyNonNullProperties(member, findMember);
//beanUtils 라는 클래스를 만들어서 그 안의 copyNonNullProperties 메서드 사용하여 안에 (수정하고자 하는 정보, 넣을 메서드명)
        //업데이트 할 필요가 있는 회원 정보가 또 있다면 위 코드는 더 길어지지만 지금 코드는 안 길어지지요
        return memberRepository.save(findMember);   // 위의 변수(업데이트된 정보)를 저장
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Memeber> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }
    // finfAll(..) 메서드는 spring에서 제공하는 메서드이고,
    // Pageable 이라는 인터페이스의 구현 객체를 파라미터로 가져요. PageRequest가 Pageable 인터페이스의 구현 클래스이구요.
    //PageRequest는 페이지네이션때 필요한 정보를 설정하는 역할을 한다고 보면 되어요.
    // 몇페이지(page)인지, 한 페이지당 데이터 개수는 몇개(size)인지, Sort by(..)로 데이터베이스 쿼리에 order by 정렬은 어떤 유형으로 할건지를 설정해준다고 보면 되어요.

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        memberRepository.delete(findMember);
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                OptionalMember.orElseThrow(() ->
                        new BusinessLogicExeption(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    private void verifyExsistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}
