package com.sukinsan.koshot.retrofit;

import com.sukinsan.koshot.response.RedmineUserResponse;

import java.io.IOException;

public interface Api {

    RedmineUserResponse redmineLoginBaseAuth(String auth) throws IOException;
}
