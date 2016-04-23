package eu.isdc.contrails.flightAwareClient.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightRoute {
	
	@JsonProperty
	private List<FlightPoint> data;

	public List<FlightPoint> getData() {
		return data;
	}

	public void setData(List<FlightPoint> data) {
		this.data = data;
	}
	
	public String toString() {
		return "FlightRoute: " + String.valueOf(data);
	}
}
