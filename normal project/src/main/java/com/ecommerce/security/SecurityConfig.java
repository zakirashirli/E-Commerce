package com.ecommerce.security;
import org.springframework.context.annotation.*; import org.springframework.http.HttpMethod; import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.crypto.bcrypt.*; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain security(HttpSecurity http,JwtFilter filter)throws Exception{return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a
  .requestMatchers("/api/auth/register","/api/auth/login","/api/contact","/uploads/**","/health","/","/*.html","/*.js","/**/*.jpg","/**/*.png","/**/*.svg","/**/*.webp","/**/*.avif").permitAll()
  .requestMatchers(HttpMethod.GET,"/api/products","/api/products/**").permitAll()
  .requestMatchers("/api/uploads/**","/api/products/my","/api/seller/**").hasRole("SELLER")
  .requestMatchers(HttpMethod.POST,"/api/products").hasRole("SELLER")
  .requestMatchers(HttpMethod.PUT,"/api/products/**").hasRole("SELLER")
  .requestMatchers(HttpMethod.DELETE,"/api/products/**").hasRole("SELLER")
  .requestMatchers("/api/**").authenticated().anyRequest().permitAll()).addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class).build();}
}
