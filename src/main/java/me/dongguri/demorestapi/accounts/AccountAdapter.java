package me.dongguri.demorestapi.accounts;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;
import java.util.stream.Collectors;

public class AccountAdapter extends User {

    private Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
        this.account = account;
    }

    private static Set<SimpleGrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(r -> {
            return new SimpleGrantedAuthority("ROLE_" + r.name());
        }).collect(Collectors.toSet());
    }

    public Account getAccount() {
        return account;
    }
}
