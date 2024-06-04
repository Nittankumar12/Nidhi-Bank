package com.RWI.Nidhi.Security;


import com.RWI.Nidhi.Security.Jwt.AuthEntryPoint;
import com.RWI.Nidhi.Security.Jwt.JwtTokenFilter;
import com.RWI.Nidhi.Security.services.UserDetailsServiceConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;

@Configuration
public class NidhiSecurityConfig {
  @Autowired
  UserDetailsServiceConfig userDetailsService;
  @Autowired
  private AuthEntryPoint unauthorizedHandler;

  @Bean
  public JwtTokenFilter authenticationJwtTokenFilter() {
    return new JwtTokenFilter();
  }

  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }


  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .cors(
                    corsconfig -> corsconfig.configurationSource(new CorsConfigurationSource() {
                      @Override
                      public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {


                          CorsConfiguration configuration = new CorsConfiguration();
                          configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173/"));
                          configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
                          configuration.setAllowedHeaders(Arrays.asList("Content-Type"
                                                               ,"x-xsrf-token"
                                                                  ,"Authorization"
                                                                 ,"Access-Control-Allow-Headers"
                                                                 ,"Access-Control-Request-Method"
                                                                  ,"Access-Control-Request-Headers"
                                                                  ,"Origin","Accept","X-Requested-With"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);
                        return configuration;

                    }})
            )
       // .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)
                              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->
          auth.requestMatchers("/home/**"
                          ,"/admin/addAdmin"
                          ,"/forget/verifyEmail"
                          ,"/forget/verifyOtp"
                          ,"/updateUserPassword"
                          ,"/ws/**"
                  ,"/user/updateProfile/**"
                  ,"/Calculator/emi/**"
                  ,"/kyc/kycdetails/**"
                  ,"/kyc/identitydocs/**"
                  ,"/kyc/address/**").permitAll().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

          .requestMatchers("/admin/**").hasAnyRole("ADMIN")
          .requestMatchers("/agent/**").hasAnyRole("AGENT","ADMIN")
          .requestMatchers("/user/**",
                  "/accounts/**",
                  "/kyc/**",
                  "/Statement**",
                  "/scheme/loan**",
                  "/rd/**",
                  "/mis/**",
                  "/loan/**",
                  "/fd/**",
                  "/Calculator/**",
                  "/email/**")
                  .hasAnyRole("USER","ADMIN","AGENT")
              .anyRequest().authenticated()
        )
            .httpBasic(Customizer.withDefaults())

//            .formLogin(form->form.loginPage("/home/signin").permitAll())
            .logout(log->log.logoutSuccessUrl("/logout"));
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
