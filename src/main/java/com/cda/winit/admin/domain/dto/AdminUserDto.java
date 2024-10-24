package com.cda.winit.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AdminUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private Boolean isAcceptTerms;
    private String requiredRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
