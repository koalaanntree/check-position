package com.wnlbs.checkposition.dataobject;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class CenterPonint implements Serializable{

	private static final long serialVersionUID = -8778421608905261291L;
	private String longitude;
	
	private String latitude;

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public CenterPonint(String longitude, String latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public CenterPonint() {
	}
}
