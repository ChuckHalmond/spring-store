package com.mygroup.springstore.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mygroup.springstore.viewmodel.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserModel implements Serializable {

	private static final long serialVersionUID = -7030478773975747154L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;
    
    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Password")
    private String password;
    
    @Column(name ="Token")
    private String token;
    
    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "Active")
    private boolean active;

    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "LastModificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModificationDate;

    @Column(name = "ResetPasswordLink")
    private String resetPasswordLink;

    @Column(name = "IsRemoved")
    private boolean isRemoved;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "Address")
    private AddressModel address;
}