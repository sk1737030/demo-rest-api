package me.dongguri.demorestapi.config;

import me.dongguri.demorestapi.accounts.AccountService;
import me.dongguri.demorestapi.common.AppProperties;
import me.dongguri.demorestapi.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseTest {
    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("이증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // When & Then
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type","password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}