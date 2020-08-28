package com.mygroup.springstore.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreListViewModel {

    private int id;
    
    private String key;

    private String name;
    
    private int ownerId;
}
