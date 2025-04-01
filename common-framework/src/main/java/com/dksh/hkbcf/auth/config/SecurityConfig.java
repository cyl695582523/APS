package com.dksh.hkbcf.auth.config;

import com.dksh.hkbcf.auth.exception.AuthException;
import com.dksh.hkbcf.auth.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AuthException authException;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginFilter loginFilter) throws Exception {
        /*

        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry.requestMatchers("/securityNone").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint));
        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);

        */


        // 关闭csrf和frameOptions，如果不关闭会影响前端请求接口（这里不展开细讲了，感兴趣的自行了解）

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));
        http.cors(Customizer.withDefaults());
        http.sessionManagement(sessionMgmt -> sessionMgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);


        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/grantToken").permitAll()
                        .anyRequest().authenticated()
                );

        //   http.exceptionHandling((exception)-> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedPage("/error/accedd-denied"));

        return http
                .sessionManagement(it-> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable) //1
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception-> exception.authenticationEntryPoint(authException))
                .logout(AbstractHttpConfigurer::disable)

                .build();

//        return http.build();

        /*
        http.csrf().disable();
        http.headers().frameOptions().disable();
        // 开启跨域以便前端调用接口
        http.cors();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http.authorizeRequests()
                // 注意这里，是允许前端跨域联调的一个必要配置
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 指定某些接口不需要通过验证即可访问。登陆、注册接口肯定是不需要认证的
                .antMatchers("/API/login", "/API/register").permitAll()
                // 这里意思是其它所有接口需要认证才能访问
                .anyRequest().authenticated()
                // 指定认证错误处理器
                .and().exceptionHandling().authenticationEntryPoint(new MyEntryPoint());
        return http.build();

         */
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("USER", "ADMIN")
                .build();
        UserDetails boss = User.builder()
                .username("boss")
                .password(passwordEncoder().encode("123456"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin, boss);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFilter loginFilter(UserDetailsService users){
        return new LoginFilter(users);
    }

    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().withUser("user")
                .password(passwordEncoder().encode("password")).roles("USER");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定UserDetailService和加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

     */
}