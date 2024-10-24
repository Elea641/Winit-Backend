package com.cda.winit.admin.domain.dto;

import lombok.*;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private Boolean isAcceptTerms;
    private String requiredRole;
}
