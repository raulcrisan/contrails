package eu.isdc.contrails.flightAwareClient;

import java.net.URI;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@Singleton
public class FlightAwareClient<T> extends RestTemplate {

	
	public static HttpEntity<String> request;
	public static Map<String, String> mapParam;
	public static String url = "http://flightxml.flightaware.com/json/FlightXML2/";
	public static String plainCreds = "raresbarbantan:e5c2120a3f9a5f75a77f1b2bc209fb9df4276dff";
    
	public FlightAwareClient() {}
		
	public FlightAwareClient(Map<String, String> mapParam) {
		super();
		FlightAwareClient.mapParam = mapParam;
		FlightAwareClient.request = createRequest();
	}
	
	public static HttpEntity<String> createRequest() {
		HttpEntity<String> request = null;
		
	    byte[] plainCredsBytes = plainCreds.getBytes();
	    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
	    String base64Creds = new String(base64CredsBytes);
	    	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Basic " + base64Creds);
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    request = new HttpEntity<String>(headers);
	    return request;
	}
	
	public static URI createURI(String endpoint){
		String fullURL = url + endpoint;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fullURL);
		mapParam.forEach((key, value) -> builder.queryParam(key, value));
		return builder.build().encode().toUri();
	}

	public ResponseEntity<T> exchange(HttpMethod method, Class<T> responseType, String endPoint) {
		URI uri = createURI(endPoint);
		return this.exchange(uri, method, FlightAwareClient.request, responseType);
	}
}
