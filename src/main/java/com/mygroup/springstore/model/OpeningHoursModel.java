
package com.mygroup.springstore.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "OpeningHours")
public class OpeningHoursModel implements Serializable {
    
	private static final long serialVersionUID = 8425959648811960278L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private int ID;

    @Column(name = "Day")
    private String day;

    @Column(name = "Begin")
    private LocalTime begin;

    @Column(name = "End")
    private LocalTime end;
    
    @Column(name = "Closed")
    private boolean closed;
    
    @ManyToMany(mappedBy = "openingHoursList")
    private List<StoreModel> storesList;
}
