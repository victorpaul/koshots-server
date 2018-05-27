package com.sukinsan.koshot.util;

public interface SecurityUtil {

    String md5(String text);

    String getSecret(String filename, long timestamp);
}
