package com.woowacourse.pelotonbackend.admin;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.pelotonbackend.member.domain.LoginFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AdminInterceptor adminInterceptor;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("충전을 기다리고 있는 회원을 모두 조회한다.")
    @Test
    void getPendingMembers() throws Exception {
        given(adminService.retrieveMemberWithPendingCash(any())).willReturn(AdminFixture.pendingMembers());
        given(adminInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class),
            any(HandlerMethod.class))).willReturn(true);

        mockMvc.perform(get("/api/admin/pending")
            .header(HttpHeaders.AUTHORIZATION, LoginFixture.getAdminTokenHeader())
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.pageable.pageSize").value(AdminFixture.TEST_PENDING_PAGE.getPageSize()))
            .andExpect(jsonPath("$.pageable.offset").value(AdminFixture.TEST_PENDING_PAGE.getOffset()));
    }

    @DisplayName("회원들의 캐시를 충전하는 요청을 성공적으로 수행한다.")
    @Test
    void updateCashes() throws Exception {
        given(adminInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class),
            any(HandlerMethod.class))).willReturn(true);

        mockMvc.perform(put("/api/admin/pending")
            .header(HttpHeaders.AUTHORIZATION, LoginFixture.getAdminTokenHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(AdminFixture.createPendingCashUpdateIds()))
        )
            .andExpect(status().isNoContent());
    }

    @DisplayName("어플의 간단한 정보를 조회할 수 있다."
        + "진행중인 레이스와 레이스에 참여중인 인원들")
    @Test
    void getAppInfo() throws Exception {
        ApplicationInfo expected = AdminFixture.createAppInfo();
        given(adminInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class),
            any(HandlerMethod.class))).willReturn(true);
        given(adminService.retrieveAppInfo()).willReturn(expected);

        mockMvc.perform(get("/api/admin/appInfo")
            .header(HttpHeaders.AUTHORIZATION, LoginFixture.getAdminTokenHeader())
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("raceCount").value(expected.getRaceCount()))
            .andExpect(jsonPath("riderCount").value(expected.getRiderCount()));
    }
}