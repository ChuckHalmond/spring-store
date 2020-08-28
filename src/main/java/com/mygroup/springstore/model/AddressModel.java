
package com.mygroup.springstore.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Addresses")
public class AddressModel implements Serializable {
	
	private static final long serialVersionUID = 2547788884110181669L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int id;

    @Column(name = "Street")
    private String street;

    @Column(name = "City")
    private String city;

    @Column(name = "State")
    private String state;

    @Column(name = "ZipCode")
    private String zipCode;

    @Column(name = "Country")
    private String country;
}