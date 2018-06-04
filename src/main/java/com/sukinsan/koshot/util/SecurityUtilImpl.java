package com.sukinsan.koshot.util;

import com.sukinsan.koshot.response.RedmineUserResponse;

import java.io.File;
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

    @Override
    public boolean isMyFile(RedmineUserResponse resp, String fileName) {
        long userID = resp.getUser().getId();
        return fileName.startsWith("id" + userID + "_");
    }

    @Override
    public File getFile(String filename) {
        return new File(getUploadFolder(),filename);
    }

    @Override
    public File getUploadFolder() {
        File uploadFOlder = new File(System.getProperty("user.home"), "koshot/uploads");
        if (!uploadFOlder.exists()) {
            uploadFOlder.mkdirs();
        }
        return uploadFOlder;
    }
}
