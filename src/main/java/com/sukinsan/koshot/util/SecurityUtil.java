package com.sukinsan.koshot.util;

import com.sukinsan.koshot.response.RedmineUserResponse;

import java.io.File;

public interface SecurityUtil {

    String md5(String text);

    String getSecret(String filename, long timestamp);

    boolean isMyFile(RedmineUserResponse resp, String fileName);

    File getFile(String filename);

    File getUploadFolder();
}
