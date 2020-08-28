package com.mygroup.springstore.mapper;

import com.mygroup.springstore.model.AddressModel;
import com.mygroup.springstore.viewmodel.AddressFormViewModel;

public class AddressMapper {

    public static AddressFormViewModel modelToFormViewModel(AddressModel am) {     
        return AddressFormViewModel
                .builder()
                .city(am.getCity())
                .country(am.getCountry())
                .state(am.getState())
                .street(am.getStreet())
                .zipCode(am.getZipCode())
                .build();
    }
       
    public static AddressModel formViewModelToModel(AddressFormViewModel avm) {
        return AddressModel
                .builder()
                .city(avm.getCity())
                .country(avm.getCountry())
                .state(avm.getState())
                .street(avm.getStreet())
                .zipCode(avm.getZipCode())
                .build();
    }
}
