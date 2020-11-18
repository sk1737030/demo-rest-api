package me.dongguri.demorestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/*@AuthenticationPrincipal(expression = "account")*/  // expression을 사용해서 필드에 있는 account를 꺼낼수있음
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")  // null 일경우 expression을 쓸수있음
public @interface CurrentUser {
}
