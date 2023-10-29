
package com.lattels.smalltour.security;


import com.lattels.smalltour.dto.LogMemberDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import eu.bitwalker.useragentutils.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.util.HashMap;
import java.util.Map;

//사용자 정보를 받아 log를 생성하는 클래스
@Slf4j
@Service
public class LogProvider {
    @Value("${secretIpInfoKey}")
    private String API_KEY;
    private final WebClient webClient = WebClient.create("https://ipinfo.io");

    public String getPublicIp(HttpServletRequest httpServletRequest){
        return webClient.get()
                .uri("/ip")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //공용 ip주소를 이용해서, 어느 지역인지 알아내는 메서드
    //내 개인 로컬 ip로하면 구할수없어서 공용 ip를 채택함
    public LogMemberDTO getRegion(HttpServletRequest httpServletRequest) throws Exception{

        WebClient webClient = WebClient.create();
        String clinetIp =  getPublicIp(httpServletRequest);
        String apiUrl = "https://ipinfo.io/" + clinetIp + "/json?token=" + API_KEY;

        LogMemberDTO response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(LogMemberDTO.class)
                .block();

        return response;
    }

   /* public String getClientIp(HttpServletRequest httpServletRequest){
        String clientIp = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = httpServletRequest.getRemoteAddr(); //ip주소 가져오기
        }

        System.out.println(clientIp);
        return clientIp;
    }*/

    public String Browser(HttpServletRequest httpServletRequest) {
        System.out.println("Browser method invoked.");

        String userAgentString = httpServletRequest.getHeader("user-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        // 브라우저 정보 추출
        Browser browser = userAgent.getBrowser();
        String browserName = browser.getName();

        if (userAgentString.contains("Whale")) {
            return "Whale";
        } else if (userAgentString.contains("Edge")) {
            return "Edge";
        } else if (userAgentString.contains("Chrome")) {
            return "Chrome";
        } else if (userAgentString.contains("Firefox")) {
            return "Firefox";
        }
        return browserName;
    }



    public String Os(HttpServletRequest httpServletRequest) {
        String userAgentString = httpServletRequest.getHeader("user-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        // 운영체제 정보 추출
        OperatingSystem os = userAgent.getOperatingSystem();
        String osName = os.getName();
        if (osName.contains("Windows 10")) {
            return "Windows 10";
        } else if (osName.contains("Linux")) {
            return "Linux";
        } else if (osName.contains("linux")) {
            return "linux";
        }
        return osName;
    }


    public String Device(HttpServletRequest httpServletRequest) {
        String userAgentString = httpServletRequest.getHeader("user-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        // 브라우저 정보 추출
        Browser browser = userAgent.getBrowser();
        BrowserType browserType = browser.getBrowserType();
        String result;
        // 접속 유형 파악 (웹 또는 앱)
        if (browserType == BrowserType.MOBILE_BROWSER || browserType == BrowserType.WEB_BROWSER) {
            result = browserType.getName().toLowerCase().replaceAll("_","");
            result = Character.toUpperCase(result.charAt(0)) + result.substring(1);

        } else{
            result = "Unknown";
        }
        return result;
    }



}

