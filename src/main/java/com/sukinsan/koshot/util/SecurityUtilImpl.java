package com.sukinsan.koshot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtilImpl implements SecurityUtil {

    @Override
    public String md5(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(text.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }

    @Override
    public String getSecret(String filename, long timestamp) {
        String secret = md5("dis is so secret" + filename + timestamp + "hidden word");
        return secret;
    }
}
