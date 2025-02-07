package org.j1p5.domain.activityArea.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.j1p5.domain.activityArea.dto.ActivityAreaAddress;
import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.activityArea.entity.QActivityArea;
import org.j1p5.domain.user.entity.QEmdArea;
import org.j1p5.domain.user.entity.QSggArea;
import org.j1p5.domain.user.entity.QSidoArea;
import org.j1p5.domain.user.entity.QUserEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ActivityAreaRepositoryCustomImpl implements ActivityAreaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ActivityAreaRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QActivityArea qActivityArea = QActivityArea.activityArea;
    QEmdArea qEmdArea = QEmdArea.emdArea;
    QSggArea qSggArea = QSggArea.sggArea;
    QSidoArea qSidoArea = QSidoArea.sidoArea;
    QUserEntity qUserEntity = QUserEntity.userEntity;

    /**
     * 커서를 기준으로 특정 상품 ID보다 작은 상품만 조회하는 조건 생성
     *
     * @param cursor 마지막으로 조회한 상품 ID
     * @return BooleanExpression (QueryDSL 조건)
     */
    private BooleanExpression cursorCondition(Long cursor) {
        if (cursor == null) {
            return null; // 커서가 없으면 조건 생략
        }
        return qEmdArea.id.lt(cursor);
    }

    private BooleanBuilder keywordCondition(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        StringTemplate fullAddress = Expressions.stringTemplate(
                "CONCAT({0}, ' ', {1}, ' ', {2})",
                qSidoArea.sidoName, qSggArea.sggName, qEmdArea.emdName
        );

        StringTemplate partialAddress = Expressions.stringTemplate(
                "CONCAT({0}, ' ', {1})",
                qSggArea.sggName, qEmdArea.emdName
        );

        return makeKeywordCondition(fullAddress, partialAddress, keyword);
    }

    private BooleanExpression userIdCondition(Long userId) {
        if (userId == null) {
            return null; // 커서가 없으면 조건 생략
        }
        return qUserEntity.id.eq(userId);
    }

    private BooleanBuilder makeKeywordCondition(StringTemplate fullAddress, StringTemplate partialAddress, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        //TODO : like검색 이외 성능 고도화
        builder.or(fullAddress.like("%" + keyword + "%"));
        builder.or(partialAddress.like("%" + keyword + "%"));
        builder.or(qSidoArea.sidoName.like(keyword + "%"));
        builder.or(qSggArea.sggName.like("%" + keyword + "%"));
        builder.or(qEmdArea.emdName.like("%" + keyword + "%"));

        return builder;
    }

    /**
     * 특정 좌표와 가까운 순서
     * @param location
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> getOrderSpecifiersByDistance(Point location) {
        String geoFunction = "ST_Distance_Sphere(coordinate, {0})";
        return new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, geoFunction, location));
    }

    @Override
    public Page<ActivityAreaAddress> getActivityAreas(Point coordinate, Pageable pageable) {
        List<ActivityAreaAddress> areaInfos =
                queryFactory
                        .selectDistinct(
                                Projections.constructor(
                                        ActivityAreaAddress.class,
                                        qEmdArea.id.as("emdId"),
                                        qSidoArea.sidoName.as("sidoName"),
                                        qSggArea.sggName.as("sggName"),
                                        qEmdArea.emdName.as("emdName")))
                        .from(qEmdArea)
                        .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                        .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                        .orderBy(getOrderSpecifiersByDistance(coordinate))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        if (areaInfos.isEmpty()) {
            return new PageImpl<>(areaInfos, pageable, 0);
        }

        Long totalCount = queryFactory
                .selectDistinct(qEmdArea.count())
                .from(qEmdArea)
                .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(areaInfos, pageable, totalCount);
    }

    @Override
    public Page<ActivityAreaAddress> getActivityAreasWithKeyword(String keyword, Pageable pageable) {
        List<ActivityAreaAddress> areaInfos =
                queryFactory
                        .selectDistinct(
                                Projections.constructor(
                                        ActivityAreaAddress.class,
                                        qEmdArea.id.as("emdId"),
                                        qSidoArea.sidoName.as("sidoName"),
                                        qSggArea.sggName.as("sggName"),
                                        qEmdArea.emdName.as("emdName")))
                        .from(qEmdArea)
                        .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                        .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                        .where(keywordCondition(keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        if (areaInfos.isEmpty()) {
            return new PageImpl<>(areaInfos, pageable, 0);
        }

        Long totalCount = queryFactory
                .selectDistinct(qEmdArea.count())
                .from(qEmdArea)
                .join(qEmdArea.sggArea).on(qSggArea.eq(qEmdArea.sggArea))
                .join(qSggArea.sidoArea).on(qSidoArea.eq(qSggArea.sidoArea))
                .where(keywordCondition(keyword))
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(areaInfos, pageable, totalCount);
    }

    @Override
    public Optional<SimpleAddress> getActivityEmdAreaByUserId(Long userId) {
        Optional<SimpleAddress> areaInfos =
                Optional.ofNullable(queryFactory.selectDistinct(
                                Projections.constructor(
                                        SimpleAddress.class,
                                        qEmdArea.id.as("emdId"),
                                        qEmdArea.emdName.as("emdName")))
                        .from(qActivityArea)
                        .join(qUserEntity).on(qActivityArea.user.eq(qUserEntity))
                        .join(qEmdArea).on(qEmdArea.eq(qActivityArea.emdArea))
                        .where(userIdCondition(userId))
                        .fetchOne());

        return areaInfos;
    }
}
