package com.bci.tareas.model;

import java.io.Serializable;


import jakarta.persistence.*;


@Entity
@Table(name = "phone"
)




public class Phone  implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private String id;
   
    @Column(name = "number", nullable = true)
	private long number;
    @Column(name = "citycode", nullable = true)
	private int cityCode;
    @Column(name = "countrycode", nullable = true)    
	private String countryCode;


    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id= id;
	}

	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public int getCityCode() {
		return cityCode;
	}
	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}  	
	
}
