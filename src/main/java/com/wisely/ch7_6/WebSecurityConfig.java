package com.wisely.ch7_6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/","/login").permitAll()	//1、设置Spring Security对/和/login路径不拦截
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")	//2、设置SpringSecurity的登录页面访问路径为/login
			.defaultSuccessUrl("/chat")	//3、登录成功后转向/chat路径。
			.permitAll()
			.and()
			.logout()
			.permitAll();
	}
	
	//4、在内存中分别配置两个用户mask和wxw，并设置他们的密码和用户名一致，角色是USER。
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("mask").password("mask").roles("USER")
			.and()
			.withUser("wxw").password("wxw").roles("USER");
	}
	
	//5、/resource/static/目录下的静态资源，SpringSecurity不拦截
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/static/**");
	}
	
	//在spring5.0以后springsecutity存储密码格式发生了变化需要加入此配置方法用于支持没有加密方式的方法，此方法已过时，不建议使用
	@Bean
	public static PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}

}
