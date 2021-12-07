package edu.northeastern.cs5520.numadfa21_happytravel.place;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.Arrays;

/** Utils for place API from google maps. */
public class PlaceUtils {
    /**
     * Try get place given a place id.
     *
     * @param placeId the place id to fetch
     * @param client the places client.
     * @return the task to find the place.
     */
    public static Task<FetchPlaceResponse> getPlace(String placeId, PlacesClient client) {
        FetchPlaceRequest request =
                FetchPlaceRequest.newInstance(
                        placeId,
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        return client.fetchPlace(request);
    }
}
