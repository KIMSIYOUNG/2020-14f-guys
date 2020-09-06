package com.woowacourse.pelotonbackend.admin;

public class AdminSql {
    public static String findPendingMembers() {
        return new StringBuilder()
            .append("SELECT PENDING_CASH.MEMBER_ID AS ID, MEMBER.NAME AS NAME")
            .append(", MEMBER.EMAIL AS EMAIL, PENDING_CASH.CASH AS CASH, PENDING_CASH.CASH_STATUS AS STATUS")
            .append(" FROM PENDING_CASH")
            .append(" LEFT JOIN MEMBER")
            .append(" ON PENDING_CASH.MEMBER_ID = MEMBER.ID")
            .append(" WHERE CASH_STATUS = (:status)")
            .append(" LIMIT :pageSize OFFSET :offset")
            .toString();
    }

    public static String countByPendingMembers() {
        return new StringBuilder()
            .append("SELECT COUNT(*)")
            .append(" FROM PENDING_CASH")
            .append(" WHERE CASH_STATUS = (:status)")
            .toString();
    }
}
