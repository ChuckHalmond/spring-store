package com.mygroup.springstore.viewmodel;

public enum UserType {
    
    ADMINISTRATOR("Administrator"),
    CLIENT("Client"),
    OWNER("Owner");

    private String displayName;

    private UserType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canEditStores() {
        return this == ADMINISTRATOR || this == OWNER;
    }
    
    public boolean canEditPromotions() {
        return this == ADMINISTRATOR || this == OWNER;
    }
}
