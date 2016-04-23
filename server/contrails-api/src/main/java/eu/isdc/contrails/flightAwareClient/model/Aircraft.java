package eu.isdc.contrails.flightAwareClient.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
public class Aircraft {

private String faFlightID;
private String ident;
private String prefix;
private String type;
private String suffix;
private String origin;
private String destination;
private String timeout;
private Integer timestamp;
private Integer departureTime;
private Integer firstPositionTime;
private Integer arrivalTime;
private Double longitude;
private Double latitude;
private Double lowLongitude;
private Double lowLatitude;
private Double highLongitude;
private Double highLatitude;
private Integer groundspeed;
private Integer altitude;
private Integer heading;
private String altitudeStatus;
private String updateType;
private String altitudeChange;
private String waypoints;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The faFlightID
*/
public String getFaFlightID() {
return faFlightID;
}

/**
* 
* @param faFlightID
* The faFlightID
*/
public void setFaFlightID(String faFlightID) {
this.faFlightID = faFlightID;
}

/**
* 
* @return
* The ident
*/
public String getIdent() {
return ident;
}

/**
* 
* @param ident
* The ident
*/
public void setIdent(String ident) {
this.ident = ident;
}

/**
* 
* @return
* The prefix
*/
public String getPrefix() {
return prefix;
}

/**
* 
* @param prefix
* The prefix
*/
public void setPrefix(String prefix) {
this.prefix = prefix;
}

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

/**
* 
* @return
* The suffix
*/
public String getSuffix() {
return suffix;
}

/**
* 
* @param suffix
* The suffix
*/
public void setSuffix(String suffix) {
this.suffix = suffix;
}

/**
* 
* @return
* The origin
*/
public String getOrigin() {
return origin;
}

/**
* 
* @param origin
* The origin
*/
public void setOrigin(String origin) {
this.origin = origin;
}

/**
* 
* @return
* The destination
*/
public String getDestination() {
return destination;
}

/**
* 
* @param destination
* The destination
*/
public void setDestination(String destination) {
this.destination = destination;
}

/**
* 
* @return
* The timeout
*/
public String getTimeout() {
return timeout;
}

/**
* 
* @param timeout
* The timeout
*/
public void setTimeout(String timeout) {
this.timeout = timeout;
}

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
* The departureTime
*/
public Integer getDepartureTime() {
return departureTime;
}

/**
* 
* @param departureTime
* The departureTime
*/
public void setDepartureTime(Integer departureTime) {
this.departureTime = departureTime;
}

/**
* 
* @return
* The firstPositionTime
*/
public Integer getFirstPositionTime() {
return firstPositionTime;
}

/**
* 
* @param firstPositionTime
* The firstPositionTime
*/
public void setFirstPositionTime(Integer firstPositionTime) {
this.firstPositionTime = firstPositionTime;
}

/**
* 
* @return
* The arrivalTime
*/
public Integer getArrivalTime() {
return arrivalTime;
}

/**
* 
* @param arrivalTime
* The arrivalTime
*/
public void setArrivalTime(Integer arrivalTime) {
this.arrivalTime = arrivalTime;
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
* The lowLongitude
*/
public Double getLowLongitude() {
return lowLongitude;
}

/**
* 
* @param lowLongitude
* The lowLongitude
*/
public void setLowLongitude(Double lowLongitude) {
this.lowLongitude = lowLongitude;
}

/**
* 
* @return
* The lowLatitude
*/
public Double getLowLatitude() {
return lowLatitude;
}

/**
* 
* @param lowLatitude
* The lowLatitude
*/
public void setLowLatitude(Double lowLatitude) {
this.lowLatitude = lowLatitude;
}

/**
* 
* @return
* The highLongitude
*/
public Double getHighLongitude() {
return highLongitude;
}

/**
* 
* @param highLongitude
* The highLongitude
*/
public void setHighLongitude(Double highLongitude) {
this.highLongitude = highLongitude;
}

/**
* 
* @return
* The highLatitude
*/
public Double getHighLatitude() {
return highLatitude;
}

/**
* 
* @param highLatitude
* The highLatitude
*/
public void setHighLatitude(Double highLatitude) {
this.highLatitude = highLatitude;
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
* The heading
*/
public Integer getHeading() {
return heading;
}

/**
* 
* @param heading
* The heading
*/
public void setHeading(Integer heading) {
this.heading = heading;
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

/**
* 
* @return
* The waypoints
*/
public String getWaypoints() {
return waypoints;
}

/**
* 
* @param waypoints
* The waypoints
*/
public void setWaypoints(String waypoints) {
this.waypoints = waypoints;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

public String toString() {
	return "Aircraft ID: " + faFlightID + "<br/>";
}
}