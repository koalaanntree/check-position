package com.wnlbs.checkposition.dataobject;

import java.io.Serializable;

public class EdgePoint implements Serializable{

	private static final long serialVersionUID = -5143883878489541856L;
	private double longitude;
	
	private double latitude;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	

}
