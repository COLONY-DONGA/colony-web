package colony.webproj.security;


import colony.webproj.category.dto.CategoryDto;
import colony.webproj.category.service.CategoryService;
import colony.webproj.entity.Role;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig { // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    //스프링 3.0부터 많이 바뀜
    //https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0

    private final PrincipalDetailService principalDetailService;

    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CategoryService categoryService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        if(CategoryDto.isFirst){
            CategoryDto.defaultCategoryDto = categoryService.getDefaultCategory();
            CategoryDto.isFirst = false;
        }


        http.csrf().disable().cors().disable() //csrf 비활성화
                .authorizeHttpRequests(request ->
                                request
                                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                        .requestMatchers("/status", "/img/**", "/css/**", "/js/**").permitAll() //정적
                                        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll() //swagger
                                        .requestMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN.name())
                                        .requestMatchers(
                                                "/login", "/join", "/login-guest", "/validation-id",
                                                "/validation-nickname", "/validation-email", "/post-list",
                                                "/post/{postId}", "/denied-page", "/time").permitAll()
//                                .requestMatchers("/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/post-list/" + CategoryDto.defaultCategoryDto.getId(), true)
                        .failureHandler(customAuthFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(customAuthenticationEntryPoint())
                                .accessDeniedHandler(customAccessDeniedHandler())
                );


        return http.build();
    }

    //https://hou27.tistory.com/entry/Spring-Security-JWT
    //https://adjh54.tistory.com/92
    //https://covenant.tistory.com/277
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}