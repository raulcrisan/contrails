package eu.isdc.contrails.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import eu.isdc.contrails.flightAwareClient.FlightAwareClient;
import eu.isdc.contrails.flightAwareClient.model.BirdsEyeResponse;
import eu.isdc.contrails.flightAwareClient.model.BirdsEyeResponseWrapper;
import eu.isdc.contrails.flightAwareClient.model.FlightPoint;
import eu.isdc.contrails.flightAwareClient.model.FlightRoute;
import eu.isdc.contrails.flightAwareClient.model.FlightRouteWrapper;
import flexjson.JSONDeserializer;
import eu.isdc.contrails.flightAwareClient.model.Aircraft;


/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@Service
public class FlightsService {

	public ResponseEntity<String> createFindFlightsInRangeRequest(String query, Integer offset) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("query", query);
    	paramMap.put("offset", String.valueOf(offset));
		
    	FlightAwareClient<String> restTemplate = new FlightAwareClient<String>(paramMap);
    	ResponseEntity<String> response = restTemplate.exchange(HttpMethod.GET, String.class, "SearchBirdseyeInFlight");
    	return response;
	}

	public String createFindFlightsInRangeRequest(String query) {
		Integer offset = 0, nextOffset = 0;
		ResponseEntity<String> response = null;
		List<Aircraft> fullList = new ArrayList<Aircraft>();
		
		while (nextOffset != -1) {
			response = createFindFlightsInRangeRequest("{range lat 46 47} {range lon 23 24}", offset);
			offset += 15;
			
			JSONDeserializer<BirdsEyeResponseWrapper> birdsEyeResponseWrapper = new JSONDeserializer<>();
			BirdsEyeResponseWrapper p = birdsEyeResponseWrapper.deserialize(response.getBody(), BirdsEyeResponseWrapper.class);
			
			nextOffset = p.getSearchBirdseyeInFlightResult().getNextOffset();
			fullList.addAll(p.getSearchBirdseyeInFlightResult().getAircraft());
		}

		if (response != null) {
			JSONDeserializer<BirdsEyeResponseWrapper> birdsEyeResponseWrapper = new JSONDeserializer<>();
			BirdsEyeResponseWrapper p = birdsEyeResponseWrapper.deserialize(response.getBody(), BirdsEyeResponseWrapper.class);
				
			fullList.addAll(p.getSearchBirdseyeInFlightResult().getAircraft());
		}
		// TODO - cache result
		
		Map<String, List<FlightPoint>> flightsDetails = new HashMap<String, List<FlightPoint>>(); 
		fullList.stream().forEach((aircraft) -> {
			if (aircraft != null && aircraft.getIdent() != null) {
				ResponseEntity<String> findFlightResponse = createFindFlightsByIdRequest(aircraft.getIdent());
	
				JSONDeserializer<FlightRouteWrapper> findFlightResponseWrapper = new JSONDeserializer<>();
				FlightRouteWrapper fpw = findFlightResponseWrapper.deserialize(findFlightResponse.getBody(), FlightRouteWrapper.class);

				Optional.ofNullable(fpw.getGetLastTrackResult()).ifPresent(
					t -> {
					flightsDetails.put(aircraft.getIdent(), fpw.getGetLastTrackResult().getData());
				});
			} 
		});
				
		return flightsDetails.toString();
	}

	public ResponseEntity<String> createFindFlightsByIdRequest(String id) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("ident", id);
		FlightAwareClient<String> restTemplate = new FlightAwareClient<String>(paramMap);
    	ResponseEntity<String> response = restTemplate.exchange(HttpMethod.GET, String.class, "GetLastTrack");
    	return response;
	}

}

