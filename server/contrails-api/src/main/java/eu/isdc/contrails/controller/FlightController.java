package eu.isdc.contrails.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import eu.isdc.contrails.flightAwareClient.model.BirdsEyeResponse;
import eu.isdc.contrails.flightAwareClient.model.BirdsEyeResponseWrapper;
import eu.isdc.contrails.service.FlightsService;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@RestController
public class FlightController {
	
	@Autowired
	private FlightsService flightsService;
	
    @RequestMapping("/flightsInRange")
    public String flightsInRange() throws UnsupportedEncodingException, IOException{
    	// params: query={range lat 46 47} {range lon 23 24}
    	// check if lat&long + radius are cached
    	return flightsService.createFindFlightsInRangeRequest("{range lat 46 47} {range lon 23 24}");
    }
    
}

