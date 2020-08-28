package com.mygroup.springstore.mapper;

import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

public class UserMapper {

    public static UserFormViewModel modelToFormViewModel(UserModel um) {
        return UserFormViewModel.builder()
                .id(um.getId())
                .firstName(um.getFirstName())
                .lastName(um.getLastName())
                .email(um.getEmail())
                .password(um.getPassword())
                .userType(um.getUserType())
                .phoneNumber(um.getPhoneNumber())
                .address(AddressMapper.modelToFormViewModel(um.getAddress()))
                .build();
    }

    public static UserModel formViewModelToModel(UserFormViewModel ufvm) {
        return UserModel.builder()
                .id(ufvm.getId())
                .firstName(ufvm.getFirstName())
                .lastName(ufvm.getLastName())
                .email(ufvm.getEmail())
                .password(ufvm.getPassword())
                .userType(ufvm.getUserType())
                .phoneNumber(ufvm.getPhoneNumber())
                .address(AddressMapper.formViewModelToModel(ufvm.getAddress()))
                .build();
    }
    
    public static UserModel applyFormViewModelOnModel(UserFormViewModel ufvm, UserModel um) {
        return um.toBuilder()
                .id(ufvm.getId())
                .firstName(ufvm.getFirstName())
                .lastName(ufvm.getLastName())
                .email(ufvm.getEmail())
                .password(ufvm.getPassword())
                .userType(ufvm.getUserType())
                .phoneNumber(ufvm.getPhoneNumber())
                .address(AddressMapper.formViewModelToModel(ufvm.getAddress()))
                .build();
    }
}
