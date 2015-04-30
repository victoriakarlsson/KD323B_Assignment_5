package se.k3.antonochisak.kd323bassignment5.api.service;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import se.k3.antonochisak.kd323bassignment5.api.model.ApiResponse;
import se.k3.antonochisak.kd323bassignment5.api.model.RootApiResponse;

import static se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers.TRAKT_API_KEY;
import static se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers.TRAKT_API_VERSION;
import static se.k3.antonochisak.kd323bassignment5.helpers.StaticHelpers.TRAKT_CONTENT_TYPE;


/**
 * Created by isak on 2015-04-24.
 */

public interface ApiService {

    String pagination = "?page=1&limit=40";

    @Headers({TRAKT_CONTENT_TYPE, TRAKT_API_KEY, TRAKT_API_VERSION})
    @GET("/movies/popular" + pagination)
    void getPopular(@Query("extended") String extended, Callback<List<ApiResponse>> callback);

    @Headers({TRAKT_CONTENT_TYPE, TRAKT_API_KEY, TRAKT_API_VERSION})
    @GET("/movies/trending" + pagination) // finished url
    void getTrending(@Query("extended") String extended, Callback<List<RootApiResponse>> callback);
}