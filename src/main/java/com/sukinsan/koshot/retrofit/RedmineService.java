package com.sukinsan.koshot.retrofit;

import com.sukinsan.koshot.response.RedmineUserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Victor on 5/21/2017.
 */

public interface RedmineService {

    @GET("users/current.json")
    Call<RedmineUserResponse> login(@Header("Authorization") String auth);

    @GET("users/current.json")
    Call<RedmineUserResponse> loginCheck(@Header("X-Redmine-API-Key") String apiKey);
}
