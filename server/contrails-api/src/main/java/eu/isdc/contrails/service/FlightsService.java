package eu.isdc.contrails.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import eu.isdc.contrails.flightAwareClient.FlightAwareClient;
import eu.isdc.contrails.flightAwareClient.model.Aircraft;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@Service
public class FlightsService {

	public ResponseEntity<Aircraft> createFindFlightsInRangeRequest(String query) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("query", query);
		FlightAwareClient<Aircraft> restTemplate = new FlightAwareClient<Aircraft>(paramMap);
    	ResponseEntity<Aircraft> response = restTemplate.exchange(HttpMethod.GET, Aircraft.class, "SearchBirdseyeInFlight");
    	return response;
	}

	public ResponseEntity<Object> createFindFlightsByIdRequest(String id) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("ident", id);
		FlightAwareClient<Object> restTemplate = new FlightAwareClient<Object>(paramMap);
    	ResponseEntity<Object> response = restTemplate.exchange(HttpMethod.GET, Object.class, "GetLastTrack");
    	return response;
	}

}

