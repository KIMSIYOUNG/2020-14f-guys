package com.woowacourse.pelotonbackend.admin;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.woowacourse.pelotonbackend.member.domain.MemberFixture;
import com.woowacourse.pelotonbackend.member.presentation.dto.MemberCashUpdateRequest;
import com.woowacourse.pelotonbackend.member.presentation.dto.MemberResponse;
import com.woowacourse.pelotonbackend.race.domain.RaceFixture;
import com.woowacourse.pelotonbackend.rider.domain.RiderFixture;
import com.woowacourse.pelotonbackend.support.AcceptanceTest;
import com.woowacourse.pelotonbackend.support.dto.JwtTokenResponse;
import com.woowacourse.pelotonbackend.vo.Cash;

public class AdminAcceptanceTest extends AcceptanceTest {

    /**
     *
     * Feature: Admin Page 관리
     *
     * Scenario: Admin Page를 관리한다.
     *
     * Given 회원이 가입되어 있다.
     * When 관리자 페이지에 접근한다.
     * Then 비인가 사용자 예외를 반환한다.
     *
     * Given 진행중인 레이스가 1개 존재한다.
     * Given 참여중인 회원들이 3명 존재한다.
     * When App info를 조회한다.
     * Then 현재 진행중인 레이스와 참여 인원을 반환한다.
     *
     * Given 충전을 기다리는 Pending Cash가 3개 존재한다.
     * When 관리자는 2개의 충전을 승인한다.
     * Then 요청을 보낸 회원의 캐시가 충전된다.
     * Then pending cash의 상태가 변경된다.
     */
    @DisplayName("띄어쓰기 단위로, 하나의 Given When Then")
    @Test
    void manageAdmin() {
        JwtTokenResponse adminToken = loginAdmin(MemberFixture.createAdminRequest());
        List<JwtTokenResponse> memberTokens = loginMembers(MemberFixture.createRequests());
        VerifyAdminInfoByMember(memberTokens.get(0));

        createRace(RaceFixture.createMockRequest(), adminToken);
        createRiders(memberTokens, RiderFixture.createMockRequest());
        fetchAppInfo(adminToken);

        createPendingCashes(memberTokens, MemberFixture.createCashUpdateRequest());
        fetchUpdatePendingCash(adminToken, AdminFixture.createPendingCashUpdateIds());
        fetchPendingCashesAndVerifyCount(adminToken);
        fetchMembersAndVerifyCash(memberTokens);

    }

    private void VerifyAdminInfoByMember(JwtTokenResponse tokenResponse) {
        given()
            .header(createTokenHeader(tokenResponse))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/admin/appInfo")
            .then()
            .log().all()
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private void fetchMembersAndVerifyCash(List<JwtTokenResponse> memberTokens) {
        List<MemberResponse> members = memberTokens.stream()
            .map(this::findMember)
            .collect(Collectors.toList());

        Cash chargedCash = MemberFixture.CASH.minus(new Cash(RaceFixture.TEST_MONEY_AMOUNT))
            .plus(MemberFixture.UPDATE_CASH);
        Cash unChargedCash = MemberFixture.CASH.minus(new Cash(RaceFixture.TEST_MONEY_AMOUNT));

        assertThat(members.get(0).getCash()).isEqualTo(chargedCash);
        assertThat(members.get(1).getCash()).isEqualTo(chargedCash);
        assertThat(members.get(2).getCash()).isEqualTo(unChargedCash);
    }

    private void fetchPendingCashesAndVerifyCount(JwtTokenResponse adminToken) {
        List<PendingMember> results = given()
            .header(createTokenHeader(adminToken))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/admin/pending")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath().getList("content", PendingMember.class);

        assertThat(results).hasSize(1);
    }

    private void fetchUpdatePendingCash(JwtTokenResponse token, PendingMemberUpdateRequest request) {
        given()
            .header(createTokenHeader(token))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .put("/api/admin/pending")
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void fetchAppInfo(JwtTokenResponse adminToken) {
        ApplicationInfo expected = ApplicationInfo.builder()
            .raceCount(1L)
            .riderCount(3L)
            .build();

        ApplicationInfo appInfo = given()
            .header(createTokenHeader(adminToken))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/admin/appInfo")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(ApplicationInfo.class);

        assertThat(appInfo).isEqualToComparingFieldByField(expected);
    }

    private void createPendingCashes(List<JwtTokenResponse> memberTokens, MemberCashUpdateRequest cashUpdateRequest) {
        memberTokens.forEach((token) -> createPendingCash(token, cashUpdateRequest));
    }

    private void createPendingCash(JwtTokenResponse token, MemberCashUpdateRequest cashUpdateRequest) {
        given()
            .header(createTokenHeader(token))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(cashUpdateRequest)
            .when()
            .patch("/api/members/cash")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
