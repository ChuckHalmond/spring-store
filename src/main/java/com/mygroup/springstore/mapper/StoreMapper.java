/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygroup.springstore.mapper;

import java.util.ArrayList;

import com.mygroup.springstore.model.OpeningHoursModel;
import com.mygroup.springstore.model.StoreModel;
import com.mygroup.springstore.viewmodel.OpeningHoursFormViewModel;
import com.mygroup.springstore.viewmodel.StoreFormViewModel;
import com.mygroup.springstore.viewmodel.StoreListViewModel;

public class StoreMapper {
  
    public static StoreFormViewModel modelToFormViewModel(StoreModel m) {

        ArrayList<OpeningHoursFormViewModel> ohvmList = new ArrayList<OpeningHoursFormViewModel>();
        
        for (OpeningHoursModel ohm : m.getOpeningHoursList()) {
            ohvmList.add(OpeningHoursMapper.modelToViewModel(ohm));
        }
        
        return StoreFormViewModel
                .builder()
                .id(m.getId())
                .key(m.getKey())
                .name(m.getName())
                .email(m.getEmail())
                .phoneNumber(m.getPhoneNumber())
                .latitude(m.getLatitude())
                .longitude(m.getLongitude())
                .address(AddressMapper.modelToFormViewModel(m.getAddress()))
                .openingHoursList(ohvmList)
                .build();
    }
    
    public static StoreModel formViewModelToModel(StoreFormViewModel sfvm) {
        ArrayList<OpeningHoursModel> ohmList = new ArrayList<OpeningHoursModel>();
        
        for (OpeningHoursFormViewModel ohvm : sfvm.getOpeningHoursList()) {
            ohmList.add(OpeningHoursMapper.viewModelToModel(ohvm));
        }
        
        return StoreModel
                .builder()
                .id(sfvm.getId())
                .key(sfvm.getKey())
                .name(sfvm.getName())
                .email(sfvm.getEmail())
                .phoneNumber(sfvm.getPhoneNumber())
                .latitude(sfvm.getLatitude())
                .longitude(sfvm.getLongitude())
                .address(AddressMapper.formViewModelToModel(sfvm.getAddress()))
                .openingHoursList(ohmList)
                .build();
    }
    
    public static StoreModel applyFormViewModelOnModel(StoreFormViewModel sfvm, StoreModel sm) {
        ArrayList<OpeningHoursModel> ohmList = new ArrayList<OpeningHoursModel>();
        
        for (OpeningHoursFormViewModel ohvm : sfvm.getOpeningHoursList()) {
            ohmList.add(OpeningHoursMapper.viewModelToModel(ohvm));
        }
        
        return sm
                .toBuilder()
                .id(sfvm.getId())
                .key(sfvm.getKey())
                .name(sfvm.getName())
                .email(sfvm.getEmail())
                .phoneNumber(sfvm.getPhoneNumber())
                .latitude(sfvm.getLatitude())
                .longitude(sfvm.getLongitude())
                .address(AddressMapper.formViewModelToModel(sfvm.getAddress()))
                .openingHoursList(ohmList)
                .build();
    }
    
    public static StoreListViewModel modelToListViewModel(StoreModel sm) {

        return StoreListViewModel
                .builder()
                .id(sm.getId())
                .key(sm.getKey())
                .name(sm.getName())
                .ownerId(sm.getOwnedBy().getId())
                .build();
    }
}
