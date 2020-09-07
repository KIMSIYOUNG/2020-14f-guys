package com.woowacourse.pelotonbackend.admin;

public class AdminSql {
    public static String findPendingMembers() {
        return new StringBuilder()
            .append("SELECT PENDING_CASH.ID AS ID, PENDING_CASH.MEMBER_ID AS MEMBER_ID, MEMBER.NAME AS NAME")
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

    public static String findPendingCashesByIds() {
        return new StringBuilder()
            .append("SELECT PENDING_CASH.ID AS ID, PENDING_CASH.MEMBER_ID AS MEMBER_ID")
            .append(" ,PENDING_CASH.CASH AS CASH, PENDING_CASH.CASH_STATUS AS CASH_STATUS")
            .append(" ,PENDING_CASH.CREATED_AT AS CREATED_AT, PENDING_CASH.UPDATED_AT AS UPDATED_AT")
            .append(" FROM PENDING_CASH")
            .append(" WHERE ID IN (:ids)")
            .toString();
    }

    public static String updatePendingStatuses() {
        return new StringBuilder()
            .append("UPDATE PENDING_CASH")
            .append(" SET CASH_STATUS = (:status)")
            .append(" WHERE ID in (:ids)")
            .toString();
    }

    public static String updateMemberCash() {
        return new StringBuilder()
            .append("UPDATE MEMBER SET CASH = CASH + (:cash)")
            .append(" WHERE ID = (:memberId)")
            .toString();
    }

    public static String countRiders() {
        return new StringBuilder()
            .append("SELECT COUNT(*)")
            .append(" FROM RIDER")
            .append(" WHERE RACE_ID IN (:raceIds)")
            .toString();
    }

    public static String countNotEndRaces() {
        return new StringBuilder()
            .append("SELECT COUNT(*)")
            .append(" FROM RACE")
            .append(" WHERE END_DATE >= (:now)")
            .toString();
    }

    public static String findNotEndRaces() {
        return new StringBuilder()
            .append("SELECT RACE.ID AS ID")
            .append(" FROM RACE")
            .append(" WHERE END_DATE >= (:now)")
            .toString();
    }
}
