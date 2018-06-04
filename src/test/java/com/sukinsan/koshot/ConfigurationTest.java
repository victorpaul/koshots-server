package com.sukinsan.koshot;

import com.sukinsan.koshot.retrofit.Api;
import com.sukinsan.koshot.retrofit.ApiImpl;
import com.sukinsan.koshot.util.SecurityUtil;
import com.sukinsan.koshot.util.SecurityUtilImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ConfigurationTest {

    @Bean
    @Primary
    public Api mockApi() {
        return Mockito.mock(ApiImpl.class);
    }

    @Bean
    @Primary
    public SecurityUtil mockSecurityUtil() {
        return Mockito.mock(SecurityUtilImpl.class);
    }

}
