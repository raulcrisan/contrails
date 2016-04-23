package eu.isdc.contrails.flightAwareClient.model;

public class FlightRouteWrapper {
	
	private FlightRoute getLastTrackResult;

    public FlightRouteWrapper() {
    }

    
    public FlightRoute getGetLastTrackResult() {
		return getLastTrackResult;
	}


	public void setGetLastTrackResult(FlightRoute getLastTrackResult) {
		this.getLastTrackResult = getLastTrackResult;
	}


	@Override
    public String toString() {
        return String.valueOf(getLastTrackResult);
    }
}
