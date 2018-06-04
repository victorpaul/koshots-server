package com.sukinsan.koshot.retrofit;

import com.sukinsan.koshot.response.RedmineUserResponse;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Created by victor on 01.08.16.
 */
public class ApiImpl implements Api {

    private RedmineService redmineService;

    public RedmineService getRedmineService() {
        if (redmineService != null) {
            return redmineService;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://redmine.ekreative.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        redmineService = retrofit.create(RedmineService.class);
        return redmineService;
    }

    @Override
    public RedmineUserResponse redmineLoginBaseAuth(String auth) throws IOException {
        Response<RedmineUserResponse> resp = getRedmineService().login(auth).execute();
        if(resp.isSuccessful()){
            return resp.body();
        }
        return null;
    }
}
