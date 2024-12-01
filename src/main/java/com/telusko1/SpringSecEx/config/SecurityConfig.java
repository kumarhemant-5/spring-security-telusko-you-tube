package com.telusko1.SpringSecEx.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // by applying it force it to not go with pre written code go through my code
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;


     @Autowired
     private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    /*
     // With Lambada expression
        // For disabling csrf
        http.csrf(customizer -> customizer.disable());
        // to make every request instance authenticated.
        http.authorizeRequests(request -> request.anyRequest().authenticated());
        // to enable login form   | after we get login from in browser but when we hit from postman then again we get login form . To make it usable we have to do below configuration.
        // http.formLogin(Customizer.withDefaults());
        // this is for postman // This will give normal pop-up for login
        http.httpBasic(Customizer.withDefaults());
        //  By  doing It we get every different session id
        //  by implementing below code on every request we get different session id, so when we hit through browser every time we get login form
        //  but we did not such thing in postman.

         NOTE: - Agar hum same cheej ko browser se karana chahen ge to hame http.formLogin(Customizer.withDefaults()) ko comment karen ge
         tab hum sirf start me ek popup get karen ge or har time alag alag session id get karen ge

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();  // build here return the object of security filter chain.


        // Without Lambada expression

        //1.
        Customizer<CsrfConfigurer<HttpSecurity>> csrfConfigurerCustomizer = new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                httpSecurityCsrfConfigurer.disable();
            }
        };
        http.csrf(csrfConfigurerCustomizer);

        // 2.
        http.authorizeRequests(request -> request.anyRequest().authenticated());

        //3.
        Customizer customizer1 = Customizer.withDefaults();
        http.formLogin(customizer1);

        //4.
        Customizer customizer = Customizer.withDefaults();
        http.httpBasic(customizer);

        //5.
        Customizer<SessionManagementConfigurer<HttpSecurity>> session = new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
            @Override
            public void customize(SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }
        };
        http.sessionManagement(session);

        return http.build();
    }

 */
        // With Builder pattern

        SecurityFilterChain securityFilterChain = http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register","logins").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        return securityFilterChain;

    }


    // Customizing the password
    // Hard coded value.

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("kiran")
//                .password("k@123")
//                .roles("USER")
//                .build();
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("harsh")
//                .password("h@123")
//                .roles("ADMIN")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(user1,user2);
//
//    }

    // Now from database

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      //  provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());  // normal comparing
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return  provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration congfig) throws Exception {

        return congfig.getAuthenticationManager();
    }






//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("kiran")
//                .password("k@123")
//                .roles("USER")
//                .build();
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("harsh")
//                .password("h@123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user3 = User
//                .withDefaultPasswordEncoder()
//                .username("Prem")
//                .password("p@123")
//                .roles("MANAGER")
//                .build();
//        return new InMemoryUserDetailsManager(user1,user2,user3);
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }


        // For JWT

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//
//
//       return config.getAuthenticationManager();
//    }

}
