package com.travel.homework.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.homework.domain.entity.QCity;
import com.travel.homework.domain.entity.QTravel;
import com.travel.homework.domain.entity.Travel;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TravelRepository {

    private final EntityManager em;

    public void save(Travel travel) {
        em.persist(travel);
    }

    public Long delete(Travel travel) {
        em.remove(travel);
        return travel.getId();
    }

    public Travel findOne(Long id) {
        return em.find(Travel.class, id);
    }

    public List<Travel> findByUserId(Long userId, LocalDateTime now) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        QTravel travel = QTravel.travel;
        QCity city = QCity.city;

        StringTemplate startDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", travel.startDate, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

        StringTemplate endDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", travel.endDate, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

        StringTemplate nowDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", now, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

        return query
                .select(travel)
                .from(travel)
                .join(travel.city, city).fetchJoin()
                .where(travel.user.id.eq(userId),
                        nowDate.between(startDate, endDate))
                .orderBy(travel.startDate.asc())
                .fetch();

    }

}
