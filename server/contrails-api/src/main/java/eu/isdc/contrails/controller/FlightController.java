package eu.isdc.contrails.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import eu.isdc.contrails.flightAwareClient.model.Aircraft;
import eu.isdc.contrails.service.FlightsService;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@RestController
public class FlightController {
	
	@Autowired
	private FlightsService flightsService;
	
    @RequestMapping("/flightsInRange")
    public String flightsInRange() {
    	// params: query={range lat 46 47} {range lon 23 24}
    	ResponseEntity<Aircraft> response = flightsService.createFindFlightsInRangeRequest("{range lat 46 47} {range lon 23 24}");
    	//cache list
//    	ResponseEntity<Object> response = flightsService.createFindFlightsByIdRequest("WZZ3716");
    	return response.getBody().toString();
    }
    
}

