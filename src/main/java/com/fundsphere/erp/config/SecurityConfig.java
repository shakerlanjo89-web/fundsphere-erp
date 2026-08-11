package com.fundsphere.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        http

            .authorizeHttpRequests(auth -> auth


                // =================================================
                // PUBLIC PAGES
                // =================================================

                .requestMatchers(

                    "/login",

                    "/member-login",

                    "/member-login/authenticate",

                    "/member-dashboard",

                    "/member-logout",

                    "/css/**",

                    "/js/**",

                    "/images/**",

                    "/uploads/**",

                    "/favicon.ico"

                ).permitAll()


                // =================================================
                // MEMBER AREA
                // =================================================

                .requestMatchers(
                    "/member-dashboard/**",
                    "/member-logout"
                )
                .permitAll()


                // =================================================
                // USER MANAGEMENT
                //
                // ONLY ADMIN / SUPER ADMIN
                // =================================================

                .requestMatchers(
                    "/user-management/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN"
                )


                // =================================================
                // SETTINGS
                //
                // ONLY ADMIN / SUPER ADMIN
                // =================================================

                .requestMatchers(
                    "/settings/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN"
                )


                // =================================================
                // MEMBERS
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT / DATA ENTRY
                // =================================================

                .requestMatchers(
                    "/members/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT",
                    "DATA_ENTRY_OPERATOR"
                )


                // =================================================
                // CONTRIBUTIONS
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT / DATA ENTRY
                // =================================================

                .requestMatchers(
                    "/contributions/**",
                    "/contribution/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT",
                    "DATA_ENTRY_OPERATOR"
                )


                // =================================================
                // BUSINESS FUND
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/business-fund/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // BUSINESS ACCOUNTING
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/business-accounting/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // EMERGENCY FUND
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/emergency-fund/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // EXPENSES
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/expenses/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // PROFIT
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/profit/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // LEDGER
                //
                // ADMIN / SUPER ADMIN / ACCOUNTANT
                // =================================================

                .requestMatchers(
                    "/ledger/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT"
                )


                // =================================================
                // REPORTS
                //
                // ALL MANAGEMENT / FINANCE ROLES
                // =================================================

                .requestMatchers(
                    "/reports/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT",
                    "COMMITTEE_MEMBER"
                )


                // =================================================
                // DASHBOARD
                //
                // LOGGED-IN STAFF
                // =================================================

                .requestMatchers(
                    "/dashboard/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "SUPER_ADMIN",
                    "ACCOUNTANT",
                    "DATA_ENTRY_OPERATOR",
                    "COMMITTEE_MEMBER"
                )


                // =================================================
                // ALL OTHER REQUESTS
                // =================================================

                .anyRequest()
                .authenticated()
            )


            // =====================================================
            // FORM LOGIN
            // =====================================================

            .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .defaultSuccessUrl(
                    "/dashboard",
                    true
                )

                .failureUrl(
                    "/login?error=true"
                )

                .permitAll()
            )


            // =====================================================
            // ACCESS DENIED
            // =====================================================

            .exceptionHandling(exception -> exception

                .accessDeniedPage(
                    "/access-denied"
                )
            )


            // =====================================================
            // LOGOUT
            // =====================================================

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl(
                    "/login?logout=true"
                )

                .invalidateHttpSession(true)

                .deleteCookies(
                    "JSESSIONID"
                )

                .permitAll()
            );


        return http.build();
    }


    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}