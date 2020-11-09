package me.dongguri.demorestapi.config;

import me.dongguri.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc // 스프링부트가 제공하는 security 사용 안함
// !! 없이하면 junit 테스트할 때 자동으로 table에 auth가 생성되어 제대로 테스트가 안된다
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() { // 토큰 저장소
        return new InMemoryTokenStore();
    }

    @Bean // 다른대에서 참조하도록 bean 으로
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override // authenticationManager를 어떻게 만들것이냐
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // security 밖에서 처리
        web.ignoring().mvcMatchers("/docs/index.html"); // 무시
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); //  정적resource 제외
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // security 안으로 들어옴
        /*http.authorizeRequests()
                .mvcMatchers("/docs/index.html").anonymous()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());*/
        http
            .anonymous()
                .and()
            .formLogin()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous() // get method는 다 가능이라는 뜻
                .anyRequest().authenticated();
    }
}
