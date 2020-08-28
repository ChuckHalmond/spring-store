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
public class AddressFormViewModel {

    @NotEmpty(message = "Please insert a street")
    private String street;
    
    @NotEmpty(message = "Please insert a city")
    private String city;
    
    @NotEmpty(message = "Please insert a state")
    private String state;
    
    @NotEmpty(message = "Please insert a zip code")
    private String zipCode;
    
    @NotEmpty(message = "Please insert a street")
    private String country;
}
