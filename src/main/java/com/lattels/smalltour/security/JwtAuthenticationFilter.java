package com.lattels.smalltour.security;

import com.lattels.smalltour.model.LogMember;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.LogMemberRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider; // 사용자 정보를 받아 JWT를 생성하는 클래스
    // 토큰을 디코딩 및 파싱하고 위조 여부를 확인 후 subject를 리턴하는 기능 포함
    @Autowired
    private LogProvider logProvider;
    @Autowired
    private LogMemberRepository logMemberRepository;
    @Autowired
    private MemberRepository memberRepository;
    /**
     *
     * logMember 저장할 메서드
     */
    public void infoFilter(HttpServletRequest request, int userId){
        try{
            String clientIp = logProvider.getClientIp(request);
            String browsers = logProvider.Browser(request);
            String os = logProvider.Os(request);
            String device = logProvider.Device(request);


            Member member = memberRepository.findByMemberId(userId);

            LogMember logMember = LogMember.builder()
                    .member(member)
                    .loginDateTime(LocalDateTime.now())
                    .browser(browsers)
                    .os(os)
                    .connectionType(device)
                    .ip(clientIp)
                    .state(0)
                    .build();

            logMemberRepository.save(logMember);
            System.out.println(logMember + "logMember");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    // doFilter 대신 스프링에선 doFilterInternal를 오버라이딩. 하지만 내부에서 토큰을 파싱해 인증한다는 점은 같음.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            // 요청에서 토큰 가져오기
            String token = //parseBearerToken(request);
                    request.getHeader("Authorization"); // Http 요청의 헤더를 파싱해 토큰을 리턴하는 함수
            // 토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
            if (token != null && !token.equalsIgnoreCase("null")){
                // userId 가져오기. 위조된 경우 예외 처리된다.
                // 토큰의 위조 여부를 확인하고 subject(userId)를 리턴하는 TokenProvider의 메서드 사용
                if(tokenProvider.validateToken(token)){
                    String userId = tokenProvider.validateAndGetUserId(token);
                    log.info("Authenticated user ID : " + userId);
                    // 인증 완료. SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                    // 이 객체에 사용자의 인증 정보를 저장
                    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, // 인증된 사용자의 정보. 문자열이 아니어도 아무것이나 넣을 수 있다. 보통은 UserDetails 라는 오브젝트를 넣음
                            null, //
                            AuthorityUtils.NO_AUTHORITIES
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // SecurityContext에 인증된 사용자를 등록
                    // 요청을 처리하는 과정에서 사용자가 인증됐는지의 여부나 인증된 사용자가 누군지 알아야 할 때가 있기 때문
                    // AuthenticationPrincipal 을 이용해 꺼내와 사용
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);

                }
            }
        } catch(Exception ex){
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request,response);
    }

    private String parseBearerToken(HttpServletRequest request){
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");
        System.out.println(bearerToken + "0000000000000000000000");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}

