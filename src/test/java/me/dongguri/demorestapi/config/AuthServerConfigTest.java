package me.dongguri.demorestapi.config;

import me.dongguri.demorestapi.accounts.Account;
import me.dongguri.demorestapi.accounts.AccountRole;
import me.dongguri.demorestapi.accounts.AccountService;
import me.dongguri.demorestapi.common.BaseControllerTest;
import me.dongguri.demorestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("이증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // Given
        String username = "dongguri@naver.com";
        String password = "dongguri";

        Account dongguri = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(dongguri);

        String clinetId = "myApp";
        String clinetSecret = "pass";

        // When & Then
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clinetId, clinetSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type","password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}