package com.cda.winit.member.domain.service.interfaces;

import com.cda.winit.member.domain.dto.MemberDto;
import com.cda.winit.member.domain.dto.MemberRequest;

public interface IMemberService {

    MemberDto createMember(String teamName, MemberRequest memberRequest);
    void deleteMember(String teamName, Long memberId) throws Exception;
}