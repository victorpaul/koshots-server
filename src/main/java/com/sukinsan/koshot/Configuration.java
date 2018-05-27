package com.sukinsan.koshot;

import com.sukinsan.koshot.util.SecurityUtil;
import com.sukinsan.koshot.util.SecurityUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public SecurityUtil provideSecurityUtil() {
        return new SecurityUtilImpl();
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
        return arrayHttpMessageConverter;
    }

    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        return list;
    }
}
