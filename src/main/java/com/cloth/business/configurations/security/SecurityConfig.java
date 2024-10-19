package com.cloth.business.configurations.security;


import com.cloth.business.configurations.jwt.JwtAuthenticationEntryPoint;
import com.cloth.business.configurations.jwt.JwtAuthenticationFilter;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()) // Enable CORS with default settings
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (if appropriate for your application)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll() //all URL starts with "/auth" can be accessed without token
                        .requestMatchers("/api/v1/test/**").permitAll() //all URL starts with "/auth" can be accessed without token
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
//                        .requestMatchers(HttpMethod.GET).permitAll() //All the GET URL will authenticated (They will not need  Token to access)
                        .anyRequest().authenticated()) // Require authentication for other endpoints

                .exceptionHandling( exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Because you are not using HTML Form base login which typically use Session, so, you have to define the Session is STATELESS. That means we are saying the program we will use Token for login, not Session for login

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);


        DefaultSecurityFilterChain build = http.build();
        return build;
    }



    @Autowired
    private CustomUserDetailsServiceImpl userDetailsServiceImple;


    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Specify the user details service and password encoder for authentication
        authenticationProvider.setUserDetailsService(userDetailsServiceImple);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder for secure password encoding
        return new CustomPasswordEncoder();
//    	return new BCryptPasswordEncoder();
    }



    // Configure and return authentication manager
    // Declare this Bean, it will needed for JWT Authentication while Login. Check Login auth controller
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    
    
    

    public class CustomPasswordEncoder implements PasswordEncoder{

        private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        
        @Override
        public String encode(CharSequence rawPassword) {
        	return bCryptPasswordEncoder.encode(rawPassword);
//        	return DigestUtils.md5Hex(rawPassword.toString()); 
        }
        

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            if (DigestUtils.md5Hex(rawPassword.toString()).equals(encodedPassword)) {
                return true;
            }
            return bCryptPasswordEncoder.matches(rawPassword.toString(), encodedPassword);
        }
    	
    }

}
