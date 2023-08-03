package com.example.stockproject.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

// filter 복습 : 요청이 들어올 때는 컨트롤러보다 먼저, 응답이 나갈 때는 컨트롤러 이후에 실행됨
// 따라서 컨트롤러에 가기 전에 요청을 가공하거나, 컨트롤러가 준 응답을 가공하기도 한다.
// 필터는 매 요청마다 반드시 실행이 된다. (그것이 필터의 개념)
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization"; // HTTP 헤더에서 어떤 키를 기준으로 토큰을 주고 받을지
    public static final String TOKEN_PREFIX = "Bearer "; // 인증 타입을 나타내기 위해 씀 jwt 의 경우 Bearer
    private final TokenProvider tokenProvider;
    // 즉, http 헤더에서 토큰은 {"Authorization" : "Bearer xxx.yyy.zzz"} 형태로 전달됨

    // 시큐리티 컨텍스트에 추가 정보를 넣는 함수
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveTokenFromRequest(request);

        // 토큰이 비어있는지, 만료되진 않았는지 검사
        if(StringUtils.hasText(token) && this.tokenProvider.validateToken(token)){
            // 유효하면 시큐리티 컨텍스트에 인증 정보 넣기
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info(String.format("[%s] -> %s", this.tokenProvider.getUsername(token), request.getRequestURI()));
        }

        filterChain.doFilter(request, response);
    }

    // http 요청으로부터 토큰을 가져오는 함수
    private String resolveTokenFromRequest(HttpServletRequest request){
        // jwt 형식에 맞는 토큰인지 검증
        String token = request.getHeader(TOKEN_HEADER);
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            // 형식에 맞으면 prefix 를 제외하고 리턴
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
