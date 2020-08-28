
package com.mygroup.springstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "Stores")
public class StoreModel implements Serializable {

	private static final long serialVersionUID = 4412739107364880342L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int id;

    @Column(name = "StoreKey", unique = true)
    private String key;

    @Column(name = "Name")
    private String name;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Email")
    private String email;

    @Column(name = "Latitude")
    private double latitude;

    @Column(name = "Longitude")
    private double longitude;

    @Column(name = "LastModifiedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @OneToOne
    @JoinColumn(name = "LastModifiedBy")
    private UserModel lastModifiedBy;
    
    @OneToOne
    @JoinColumn(name = "OwnedBy")
    private UserModel ownedBy;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "Address")
    private AddressModel address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "Stores_OpeningHours", joinColumns = {
        @JoinColumn(name = "storesList", referencedColumnName = "ID")
    }, inverseJoinColumns = {
        @JoinColumn(name = "openingHoursList", referencedColumnName = "ID")
    })
    private List<OpeningHoursModel> openingHoursList;
}