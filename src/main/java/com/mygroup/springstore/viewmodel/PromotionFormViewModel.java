package com.mygroup.springstore.viewmodel;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionFormViewModel {

    private int id;

    @NotEmpty(message = "Please insert a key")
    private String key;

    @NotEmpty(message = "Please insert a title")
    private String title;

    @NotEmpty(message = "Please insert a short description")
    private String shortDescription;

    @NotEmpty(message = "Please insert a long description")
    private String longDescription;

    @NotEmpty(message = "Please insert a position")
    private String position;

    private boolean disabled;

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private Date endDate;

    private byte[] image; 
}
