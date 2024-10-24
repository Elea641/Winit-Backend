package com.cda.winit.member.application;

import com.cda.winit.member.domain.dto.MemberDto;
import com.cda.winit.member.domain.dto.MemberRequest;
import com.cda.winit.member.domain.service.MemberService;
import com.cda.winit.member.infrastructure.exception.MemberServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{teamName}")
    public ResponseEntity<?> createMember(@PathVariable String teamName, @RequestBody MemberRequest memberRequest) {
        try {
            MemberDto memberDto = memberService.createMember(teamName, memberRequest);
            return ResponseEntity.ok().body(memberDto);
        } catch (MemberServiceException ex) {
            return ResponseEntity.badRequest().body("Erreur : " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Une erreur s'est produite lors de l'inscription du membre dans l'équipe : " + ex.getMessage());
        }
    }

    @DeleteMapping("/{teamName}/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable String teamName, @PathVariable String memberId) {
        try {
            Long memberIdConvert = Long.parseLong(memberId);
            memberService.deleteMember(teamName, memberIdConvert);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Le membre a bien été supprimé de l'équipe"));
        } catch (MemberServiceException ex) {
            return ResponseEntity.badRequest().body("Erreur : " + ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}