package com.woowacourse.pelotonbackend.admin;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import com.woowacourse.pelotonbackend.pendingcash.CashStatus;
import com.woowacourse.pelotonbackend.pendingcash.PendingCash;

@Repository
public class AdminRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final EntityRowMapper<PendingMember> rowMapper;
    private final EntityRowMapper<PendingCash> cashRowMapper;

    @SuppressWarnings("unchecked")
    public AdminRepository(
        final NamedParameterJdbcOperations jdbcOperations,
        final RelationalMappingContext mappingContext,
        final JdbcConverter jdbcConverter) {
        this.jdbcOperations = jdbcOperations;

        this.rowMapper = new EntityRowMapper<>(
            (RelationalPersistentEntity<PendingMember>)mappingContext.getRequiredPersistentEntity(PendingMember.class),
            jdbcConverter
        );

        this.cashRowMapper = new EntityRowMapper<>(
            (RelationalPersistentEntity<PendingCash>)mappingContext.getRequiredPersistentEntity(PendingCash.class),
            jdbcConverter
        );
    }

    public Page<PendingMember> findMembersByPendingCash(Pageable pageable) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("status", CashStatus.PENDING.name())
            .addValue("offset", pageable.getOffset())
            .addValue("pageSize", pageable.getPageSize());

        List<PendingMember> membersWithoutPage = this.jdbcOperations.query(AdminSql.findPendingMembers(),
            parameterSource, rowMapper);

        return PageableExecutionUtils.getPage(membersWithoutPage, pageable, () ->
            this.jdbcOperations.queryForObject(AdminSql.countByPendingMembers(), parameterSource, Long.class));
    }

    public List<PendingCash> findPendingCashes(List<Long> ids) {
        MapSqlParameterSource source = new MapSqlParameterSource()
            .addValue("ids", ids);

        return this.jdbcOperations.query(AdminSql.findPendingCashesByIds(), source, cashRowMapper);
    }

    public void updatePendingStatuses(List<Long> pendingCashIds) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("status", CashStatus.RESOLVE.name())
            .addValue("ids", pendingCashIds);

        this.jdbcOperations.update(AdminSql.updatePendingStatuses(), parameterSource);
    }

    public void updateMembersCash(Long memberId, BigDecimal cash) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("memberId", memberId)
            .addValue("cash", cash);

        this.jdbcOperations.update(AdminSql.updateMemberCash(), parameterSource);
    }
}
