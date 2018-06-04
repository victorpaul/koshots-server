package com.sukinsan.koshot.controller;

import com.sukinsan.koshot.util.SecurityUtil;
import com.sukinsan.koshot.util.SecurityUtilImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecurityUtilTest {

    private SecurityUtil securityUtil = new SecurityUtilImpl();

    @Test
    public void md5_works_well() {
        assertEquals("202cb962ac59075b964b07152d234b70",securityUtil.md5("123"));
        assertEquals("d8578edf8458ce06fbc5bb76a58c5ca4",securityUtil.md5("qwerty"));
    }

    @Test
    public void user_can_edit_file() {
        String filename = "image.jpg";
        long timeStamp = 1528142220778l;
        assertEquals("3925571707e075641232f24c166a6c67",securityUtil.getSecret(filename,timeStamp));

    }
}
