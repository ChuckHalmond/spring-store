package com.mygroup.springstore.mapper;

import com.mygroup.springstore.model.OpeningHoursModel;
import com.mygroup.springstore.viewmodel.OpeningHoursFormViewModel;

public class OpeningHoursMapper {

    public static OpeningHoursFormViewModel modelToViewModel(OpeningHoursModel ohm) {
        return OpeningHoursFormViewModel
                .builder()
                .day(ohm.getDay())
                .begin(ohm.getBegin())
                .end(ohm.getEnd())
                .closed(ohm.isClosed())
                .build();
    }

    public static OpeningHoursModel viewModelToModel(OpeningHoursFormViewModel ohvm) {
        return OpeningHoursModel
            .builder()
            .day(ohvm.getDay())
            .begin(ohvm.getBegin())
            .end(ohvm.getEnd())
            .closed(ohvm.isClosed())
            .build();
    }
}
