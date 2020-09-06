package com.woowacourse.pelotonbackend.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.woowacourse.pelotonbackend.common.exception.UnAuthenticatedException;
import com.woowacourse.pelotonbackend.member.application.MemberService;
import com.woowacourse.pelotonbackend.member.presentation.dto.MemberResponse;
import com.woowacourse.pelotonbackend.support.AuthorizationExtractor;
import com.woowacourse.pelotonbackend.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final AuthorizationExtractor extractor;
    private final JwtTokenProvider provider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String value = extractor.extract(request);
        String socialId = provider.getSubject(value);

        MemberResponse member = memberService.findByKakaoId(Long.parseLong(socialId));
        if (member.getRole().isNotAdmin()) {
            throw new UnAuthenticatedException(member.getId());
        }

        return true;
    }
}
