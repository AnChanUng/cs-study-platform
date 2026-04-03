---
title: "Spring Security"
category: spring
difficulty: ADVANCED
tags: "Spring Security, 인증, 인가, JWT, 필터 체인"
---

## 질문
Spring Security의 동작 원리와 인증/인가 과정에 대해 설명해주세요.

## 핵심 키워드
- 인증 (Authentication)
- 인가 (Authorization)
- SecurityFilterChain
- UserDetailsService
- JWT

## 답변
Spring Security는 Spring 기반 애플리케이션의 보안(인증, 인가)을 담당하는 프레임워크입니다. 서블릿 필터 기반으로 동작하며, 요청이 DispatcherServlet에 도달하기 전에 보안 처리를 수행합니다.

### 인증 vs 인가

- **인증(Authentication)**: "누구인가?" - 사용자의 신원 확인 (로그인)
- **인가(Authorization)**: "무엇을 할 수 있는가?" - 권한 확인 (접근 제어)

### Security Filter Chain

```
HTTP 요청 → [Security Filter Chain] → DispatcherServlet

주요 필터:
1. SecurityContextPersistenceFilter - SecurityContext 로드
2. UsernamePasswordAuthenticationFilter - 폼 로그인 처리
3. BasicAuthenticationFilter - HTTP Basic 인증
4. BearerTokenAuthenticationFilter - JWT 등 토큰 인증
5. ExceptionTranslationFilter - 보안 예외 처리
6. FilterSecurityInterceptor - 인가 처리
```

### 인증 흐름

```
1. 사용자 로그인 요청 (username, password)
2. AuthenticationFilter가 인증 요청 가로챔
3. UsernamePasswordAuthenticationToken 생성
4. AuthenticationManager에게 인증 위임
5. AuthenticationProvider가 UserDetailsService를 통해 사용자 정보 조회
6. 비밀번호 검증 (PasswordEncoder)
7. 인증 성공 → SecurityContext에 Authentication 저장
8. 이후 요청에서 SecurityContext를 통해 인증 정보 확인
```

### Security 설정

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### JWT 기반 인증

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws Exception {
        String token = extractToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
```

### 세션 기반 vs 토큰 기반 인증

| 구분 | 세션 | JWT |
|------|------|-----|
| 저장 위치 | 서버 | 클라이언트 |
| 상태 | Stateful | Stateless |
| 확장성 | 서버 간 세션 공유 필요 | 확장 용이 |
| 보안 | 서버에서 무효화 가능 | 만료 전 무효화 어려움 |

### 면접 팁
Spring Security의 필터 체인 구조와 인증 흐름을 순서대로 설명할 수 있어야 합니다. JWT와 세션 방식의 차이점, 각각의 장단점을 비교할 수 있으면 좋습니다. CORS, CSRF 처리 방법도 알아두세요.

## 꼬리 질문
1. JWT의 구조(Header, Payload, Signature)에 대해 설명해주세요.
2. JWT의 만료 전 무효화(로그아웃)를 어떻게 구현하나요?
3. CSRF 공격이란 무엇이며, 어떻게 방어하나요?
