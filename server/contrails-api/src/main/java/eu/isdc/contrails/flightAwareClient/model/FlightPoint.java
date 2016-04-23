package eu.isdc.contrails.flightAwareClient.model;

public class FlightPoint {

	private Integer timestamp;
	private Double latitude;
	private Double longitude;
	private Integer groundspeed;
	private Integer altitude;
	private String altitudeStatus;
	private String updateType;
	private String altitudeChange;

	/**
	* 
	* @return
	* The timestamp
	*/
	public Integer getTimestamp() {
	return timestamp;
	}

	/**
	* 
	* @param timestamp
	* The timestamp
	*/
	public void setTimestamp(Integer timestamp) {
	this.timestamp = timestamp;
	}

	/**
	* 
	* @return
	* The latitude
	*/
	public Double getLatitude() {
	return latitude;
	}

	/**
	* 
	* @param latitude
	* The latitude
	*/
	public void setLatitude(Double latitude) {
	this.latitude = latitude;
	}

	/**
	* 
	* @return
	* The longitude
	*/
	public Double getLongitude() {
	return longitude;
	}

	/**
	* 
	* @param longitude
	* The longitude
	*/
	public void setLongitude(Double longitude) {
	this.longitude = longitude;
	}

	/**
	* 
	* @return
	* The groundspeed
	*/
	public Integer getGroundspeed() {
	return groundspeed;
	}

	/**
	* 
	* @param groundspeed
	* The groundspeed
	*/
	public void setGroundspeed(Integer groundspeed) {
	this.groundspeed = groundspeed;
	}

	/**
	* 
	* @return
	* The altitude
	*/
	public Integer getAltitude() {
	return altitude;
	}

	/**
	* 
	* @param altitude
	* The altitude
	*/
	public void setAltitude(Integer altitude) {
	this.altitude = altitude;
	}

	/**
	* 
	* @return
	* The altitudeStatus
	*/
	public String getAltitudeStatus() {
	return altitudeStatus;
	}

	/**
	* 
	* @param altitudeStatus
	* The altitudeStatus
	*/
	public void setAltitudeStatus(String altitudeStatus) {
	this.altitudeStatus = altitudeStatus;
	}

	/**
	* 
	* @return
	* The updateType
	*/
	public String getUpdateType() {
	return updateType;
	}

	/**
	* 
	* @param updateType
	* The updateType
	*/
	public void setUpdateType(String updateType) {
	this.updateType = updateType;
	}

	/**
	* 
	* @return
	* The altitudeChange
	*/
	public String getAltitudeChange() {
	return altitudeChange;
	}

	/**
	* 
	* @param altitudeChange
	* The altitudeChange
	*/
	public void setAltitudeChange(String altitudeChange) {
	this.altitudeChange = altitudeChange;
	}

	public String toString(){
		return "point: lat:" + getLatitude() + " ; long: " + getLongitude() + "<br/>";
	};
}

