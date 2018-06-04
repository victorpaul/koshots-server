package com.sukinsan.koshot.controller;

import com.sukinsan.koshot.KoshotApplication;
import com.sukinsan.koshot.entity.RedmineUserEntity;
import com.sukinsan.koshot.response.RedmineUserResponse;
import com.sukinsan.koshot.retrofit.Api;
import com.sukinsan.koshot.util.SecurityUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KoshotApplication.class})
@WebAppConfiguration
public class KoshotApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Api mockApi;

    @Autowired
    private SecurityUtil securityUtil;

    private MockMvc mockMvc;

    private RedmineUserResponse successLoginResponse;
    private RedmineUserEntity loggedInUser;

    @Before
    public void setup() {
        loggedInUser = mock(RedmineUserEntity.class);
        successLoginResponse = mock(RedmineUserResponse.class);
        when(successLoginResponse.getUser()).thenReturn(loggedInUser);
        when(loggedInUser.getId()).thenReturn(1l);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void ping_got_pong() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    public void pong_got_wrong() throws Exception {
        mockMvc.perform(get("/pong")).andExpect(status().isNotFound());
    }

    @Test
    public void check_bad_request() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/shot").file("file", "body".getBytes()))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/shot/name1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/shot/name1/publicurl"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/shots"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/shot/name1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void check_unauthorized() throws Exception {
        when(mockApi.redmineLoginBaseAuth(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/shot").file("file", "body".getBytes()).header("Authorization", "qwe1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/shot/name1").header("Authorization", "qwe2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/shot/name1/publicurl").header("Authorization", "qwe3"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/shots").header("Authorization", "qwe4"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/shot/name1").header("Authorization", "qwe5"))
                .andExpect(status().isUnauthorized());

        verify(mockApi).redmineLoginBaseAuth("qwe1");
        verify(mockApi).redmineLoginBaseAuth("qwe2");
        verify(mockApi).redmineLoginBaseAuth("qwe3");
        verify(mockApi).redmineLoginBaseAuth("qwe4");
        verify(mockApi).redmineLoginBaseAuth("qwe5");
    }

    @Test
    public void get_public_url() throws Exception {
        when(mockApi.redmineLoginBaseAuth("qwe")).thenReturn(successLoginResponse);
        when(securityUtil.isMyFile(successLoginResponse,"name1")).thenReturn(true);
        when(securityUtil.getFile("name1")).thenCallRealMethod();
        mockMvc.perform(get("/api/shot/name1/publicurl").header("Authorization", "qwe"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fail_to_get_public_url() throws Exception {
        when(mockApi.redmineLoginBaseAuth("qwe")).thenReturn(successLoginResponse);
        when(securityUtil.isMyFile(successLoginResponse,"name1")).thenReturn(false);
        mockMvc.perform(get("/api/shot/name1/publicurl").header("Authorization", "qwe"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void check_get_shots() throws Exception {
        when(mockApi.redmineLoginBaseAuth("qwe")).thenReturn(successLoginResponse);
        when(securityUtil.getUploadFolder()).thenCallRealMethod();
        mockMvc.perform(get("/api/shots").header("Authorization", "qwe"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"shots\":[]}"));
    }

    @Test
    public void delete_my_file_success() throws Exception {
        when(mockApi.redmineLoginBaseAuth("qwe")).thenReturn(successLoginResponse);
        when(securityUtil.isMyFile(successLoginResponse, "name1")).thenReturn(true);
        when(securityUtil.getFile("name1")).thenCallRealMethod();
        mockMvc.perform(delete("/api/shot/name1").header("Authorization", "qwe"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fail_on_deleting_someones_file() throws Exception {
        when(mockApi.redmineLoginBaseAuth("qwe")).thenReturn(successLoginResponse);
        when(securityUtil.isMyFile(successLoginResponse, "name1")).thenReturn(false);
        mockMvc.perform(delete("/api/shot/name1").header("Authorization", "qwe"))
                .andExpect(status().isUnauthorized());
    }

}
