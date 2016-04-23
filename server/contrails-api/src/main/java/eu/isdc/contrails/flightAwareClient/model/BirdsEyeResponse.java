package eu.isdc.contrails.flightAwareClient.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BirdsEyeResponse {

	@JsonProperty
	private Integer next_offset;
	@JsonProperty
	private List<Aircraft> aircraft = new ArrayList<Aircraft>();
	
	public Integer getNextOffset() {
		return next_offset;
	}
	
	public void setNext_offset(Integer nextOffset) {
		this.next_offset = nextOffset;
	}
	
	public List<Aircraft> getAircraft() {
		return aircraft;
	}
	
	public void setAircraft(List<Aircraft> aircraft) {
		this.aircraft = aircraft;
	}

	public String toString() {
		return "BirdsEyeObject: " + String.valueOf(next_offset);
	}
}