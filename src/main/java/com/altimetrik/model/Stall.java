package com.altimetrik.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="StallData")
public class Stall {
	@Transient
	private boolean errorStatus=false;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stallId;
	
	@Column(name="stallName",length = 30)
	private String stallName;
	@Column(name="stallType",length = 30)
	private String stallType;
	@Column(name="stallOwner",length = 30)
	private String stallOwner;
	@Column(name="stallFare")
	private Double stallFare;
	
	public Integer getStallId() {
		return stallId;
	}
	public void setStallId(Integer stallId) {
		this.stallId = stallId;
	}	
	public void setStallId(String stallId) {
		try {
			this.stallId = Integer.valueOf(stallId);
		}catch(Exception e) {
			
		}
	}
	public String getStallName() {
		return stallName;
	}
	public void setStallName(String stallName) {
		this.stallName = stallName;
	}
	public String getStallType() {
		return stallType;
	}
	public void setStallType(String stallType) {
		this.stallType = stallType;
	}
	public String getStallOwner() {
		return stallOwner;
	}
	public void setStallOwner(String stallOwner) {
		this.stallOwner = stallOwner;
	}
	public Double getStallFare() {
		return stallFare;
	}
	public boolean getStatus() {
		return errorStatus;
	}
	public void setStatus(boolean status) {
		this.errorStatus = status;
	}
	public void setStallFare(Double stallFare) {
		this.stallFare = stallFare;
	}
	public void setStallFare(String stallFare) {
		try {
			this.stallFare = Double.valueOf(stallFare);
		}catch(Exception e) {
			this.errorStatus=true;
		}
	}
	public Stall() {
		this.errorStatus=false;
	}
	
	
	@Override
	public String toString() {
		
		return this.getStallId()+"-"+this.getStallName()+"-"+this.getStallOwner()+"-"+this.getStallType()+"-"+this.getStallFare();
	}
}
