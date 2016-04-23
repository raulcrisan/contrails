package eu.isdc.contrails.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import eu.isdc.contrails.flightAwareClient.model.Quote;
import eu.isdc.contrails.test.TestRest;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public TestRest greeting(@RequestParam(value="name", defaultValue="World") String name) {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        return new TestRest(counter.incrementAndGet(), quote.toString());
    }
}