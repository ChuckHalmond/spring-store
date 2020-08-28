package com.mygroup.springstore.viewmodel;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreFormViewModel {

    private int id;
    
    @NotEmpty(message = "Please insert a key")
    private String key;

    @NotEmpty(message = "Please insert a name")
    private String name;

    @NotEmpty(message = "Please insert a phoneNumber")
    private String phoneNumber;

    @NotEmpty(message = "Please insert an email")
    private String email;

    private double latitude;

    private double longitude;

    private AddressFormViewModel address;
    
    private List<OpeningHoursFormViewModel> openingHoursList;

    public void initializeOpeningHoursList() {
        this.openingHoursList = new ArrayList<OpeningHoursFormViewModel>();
        for (int dayIdx = 1; dayIdx <= 6; dayIdx++) {
            openingHoursList.add(
                new OpeningHoursFormViewModel(
                    DayOfWeek.of(dayIdx).getDisplayName( 
                        TextStyle.SHORT, 
                        Locale.US
                    ),
                    LocalTime.of(8, 0),
                    LocalTime.of(18, 0),
                    false
                )
            );
        }
    }
}