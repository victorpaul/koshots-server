package com.sukinsan.koshot;

import com.sukinsan.koshot.util.SecurityUtil;
import com.sukinsan.koshot.util.SecurityUtilImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KoshotApplicationTests {

    @Test
    public void contextLoads() {
        SecurityUtil su = new SecurityUtilImpl();
        assertEquals("5d41402abc4b2a76b9719d911017c592",su.md5("hello"));
        assertEquals("7d793037a0760186574b0282f2f435e7",su.md5("world"));
    }

}
