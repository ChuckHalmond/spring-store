package com.mygroup.springstore.viewmodel;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginFormViewModel {

    @NotEmpty(message = "Please insert your email address")
    private String email;
    
    @NotEmpty(message = "Please insert your password")
    private String password;
}