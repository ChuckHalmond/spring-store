package com.mygroup.springstore.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Promotions")
public class PromotionModel implements Serializable {

	private static final long serialVersionUID = -8666248002347482709L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int id;

    @Column(name = "PromotionKey", unique = true)
    private String key;

    @Column(name = "Title")
    private String title;

    @Column(name = "ShortDescription")
    private String shortDescription;

    @Column(name = "LongDescription")
    private String longDescription;

    @Column(name = "Position")
    private String position;

    @Column(name = "Disabled")
    private boolean disabled;

    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "Image", columnDefinition = "BLOB")
    private byte[] image;
}
