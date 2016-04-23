package eu.isdc.contrails.flightAwareClient.model;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
public class BirdsEyeResponseWrapper {

    private BirdsEyeResponse SearchBirdseyeInFlightResult;

    public BirdsEyeResponseWrapper() {
    }

    public BirdsEyeResponse getSearchBirdseyeInFlightResult() {
		return SearchBirdseyeInFlightResult;
	}

	public void setSearchBirdseyeInFlightResult(BirdsEyeResponse searchBirdseyeInFlightResult) {
		SearchBirdseyeInFlightResult = searchBirdseyeInFlightResult;
	}

	@Override
    public String toString() {
        return String.valueOf(SearchBirdseyeInFlightResult);
    }
}