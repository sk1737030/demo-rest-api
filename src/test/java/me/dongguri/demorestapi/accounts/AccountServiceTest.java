package me.dongguri.demorestapi.accounts;

import me.dongguri.demorestapi.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;


public class AccountServiceTest extends BaseTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @Test
    public void findByUserName() {
        // Given
        String password = "dongguri";
        String userName = "dongguri@email.com";
        Account account = Account.builder()
                .email(userName)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);


        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isEqualTo(true);
    }


    @Test
    public void findByUserNameFail() {
        String username = "sk";

        // Given

        // when


        assertThrows(UsernameNotFoundException.class, () -> {
            accountService.loadUserByUsername(username);
        });
    }

}
