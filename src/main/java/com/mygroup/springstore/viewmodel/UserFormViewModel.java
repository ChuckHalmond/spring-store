package com.mygroup.springstore.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFormViewModel {

    private int id;
    
    @NotEmpty(message = "Firstname can't be empty")
    private String firstName;
    
    @NotEmpty(message = "Firstname can't be empty")
    private String lastName;
    
    @NotEmpty(message = "Email can't  be empty")
    private String email;
    
    @NotEmpty(message = "Password can't cannot be empty")
    private String password;
    
    @NotEmpty(message = "Phone number can't be empty")
    private String phoneNumber;

    private AddressFormViewModel address;

    private UserType userType;

    public static ArrayList<UserType> getUserTypesList() {
        ArrayList<UserType> userTypes = new ArrayList<UserType>();
        
        userTypes.addAll(Arrays.asList(UserType.values()));
        
        return userTypes;
    }
}
