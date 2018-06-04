package com.sukinsan.koshot.retrofit;

import com.sukinsan.koshot.response.RedmineUserResponse;
import retrofit2.Response;

import java.io.IOException;

public interface Api {

    Response<RedmineUserResponse> redmineLoginBaseAuth(String auth) throws IOException;
}
