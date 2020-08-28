package com.mygroup.springstore.viewmodel;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHoursFormViewModel {

    private String day;

    private LocalTime begin;

    private LocalTime end;
    
    private boolean closed;
}
