package com.codestates.member.mapper;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto .Post requestBody);

    Member memberPatchDtoToMember(MemberDto.Patch requestBody);

    @Mapping(source = "member.stamp.stampCount", target = "stampCount")
    @Mapping(source = "member.memberStatus.status", target = "memberStatus")
//    매핑 타입을 맞춰주는건데 source의 member.memberStatus.status 에서 member는 Order 클래스 같긴한데 암튼 Order 클래스의 member 필드이고,
//    memberStatus는 Member 클래스 안에 MemberStatus라는 enum을 의미하고
//    status는 MemberStatus enum의 status를 의미해요. 결국 source의 타입과 target의 타입을 맞춰주는거라는..

    MemberDto.Response memberToMemberResponseDto(Member.member);

    List<MemberDto.Response> membersToMemberResponseDtos(List<Member> members);

}
