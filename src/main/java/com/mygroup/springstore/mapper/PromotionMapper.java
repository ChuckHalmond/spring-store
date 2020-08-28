package com.mygroup.springstore.mapper;

import com.mygroup.springstore.model.PromotionModel;
import com.mygroup.springstore.viewmodel.PromotionFormViewModel;
import com.mygroup.springstore.viewmodel.PromotionListViewModel;

public class PromotionMapper {
  
    public static PromotionFormViewModel modelToFormViewModel(PromotionModel pm) {
        return PromotionFormViewModel
                .builder() 
                .id(pm.getId())
                .key(pm.getKey())
                .title(pm.getTitle())
                .shortDescription(pm.getShortDescription())
                .longDescription(pm.getLongDescription())
                .position(pm.getPosition())
                .disabled(pm.isDisabled())
                .startDate(pm.getStartDate())
                .endDate(pm.getEndDate())
                .image(pm.getImage())
                .build();
    }
    
    public static PromotionModel formViewModelToModel(PromotionFormViewModel pfvm) {
        return PromotionModel
                .builder()
                .id(pfvm.getId())
                .key(pfvm.getKey())
                .title(pfvm.getTitle())
                .shortDescription(pfvm.getShortDescription())
                .longDescription(pfvm.getLongDescription())
                .position(pfvm.getPosition())
                .disabled(pfvm.isDisabled())
                .startDate(pfvm.getStartDate())
                .endDate(pfvm.getEndDate())
                .image(pfvm.getImage())
                .build();
    }
    
    public static PromotionModel applyFormViewModelOnModel(PromotionFormViewModel pfvm, PromotionModel pm) {
        return pm
                .toBuilder()
                .id(pfvm.getId())
                .key(pfvm.getKey())
                .title(pfvm.getTitle())
                .shortDescription(pfvm.getShortDescription())
                .longDescription(pfvm.getLongDescription())
                .position(pfvm.getPosition())
                .disabled(pfvm.isDisabled())
                .startDate(pfvm.getStartDate())
                .endDate(pfvm.getEndDate())
                .image(pfvm.getImage())
                .build();
    }
    
    public static PromotionListViewModel modelToListViewModel(PromotionModel pm) {
        return PromotionListViewModel
                .builder()
                .id(pm.getId())
                .key(pm.getKey())
                .title(pm.getTitle())
                .build();
    }
}
