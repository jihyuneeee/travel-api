package com.travel.homework.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.homework.domain.entity.City;
import com.travel.homework.domain.entity.QCity;
import com.travel.homework.domain.entity.QTravel;

import javax.persistence.*;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CityRepository {

        private final EntityManager em;

        public void save(City city) {
                em.persist(city);
        }

        public List<City> findByCityname(String cityname) {
                return em.createQuery("select c from City c where c.cityname = :cityname", City.class)
                                .setParameter("cityname", cityname)
                                .getResultList();
        };

        public List<City> findById(Long id) {

                return em.createQuery("select c from City c join fetch c.travels t where c.id = :id ",
                                City.class)
                                .setParameter("id", id)
                                .getResultList();
        };

        public List<City> findByUserId(Long userId, LocalDateTime now) {

                JPAQueryFactory query = new JPAQueryFactory(em);

                QCity city = QCity.city;
                QTravel travel = QTravel.travel;

                StringTemplate startDate = Expressions.stringTemplate(
                                "DATE_FORMAT({0}, {1})", travel.startDate, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

                StringTemplate endDate = Expressions.stringTemplate(
                                "DATE_FORMAT({0}, {1})", travel.endDate, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

                StringTemplate regDate = Expressions.stringTemplate(
                                "DATE_FORMAT({0}, {1})", city.regDate, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

                StringTemplate nowDate = Expressions.stringTemplate(
                                "DATE_FORMAT({0}, {1})", now, ConstantImpl.create("%Y-%m-%d HH:mm:ss"));

                StringPath alias = Expressions.stringPath("rank");

                List<Tuple> result = query
                                .select(city,
                                                new CaseBuilder()
                                                                .when(startDate.gt(nowDate)).then(1)
                                                                .when(regDate.eq(nowDate)).then(2)
                                                                .when(inqryDateAfter(now)).then(3)
                                                                .otherwise(4).as("rank"))
                                .from(city).join(city.travels, travel).fetchJoin()
                                .where(travel.user.id.eq(userId), nowDate.notBetween(startDate, endDate))
                                .orderBy(alias.asc())
                                .orderBy(new CaseBuilder()
                                                .when(startDate.gt(nowDate)).then(travel.startDate)
                                                .otherwise(travel.startDate).asc())
                                .orderBy(new CaseBuilder()
                                                .when(regDate.eq(nowDate)).then(city.regDate).otherwise(city.regDate)
                                                .asc())
                                .orderBy(new CaseBuilder()
                                                .when(inqryDateAfter(now)).then(city.inqryDate)
                                                .otherwise(city.inqryDate)
                                                .asc())

                                .fetch();

                List<City> list = new ArrayList<>();

                for (Tuple tuple : result) {
                        City city2 = tuple.get(city);
                        list.add(city2);
                }

                return list;

        }

        public BooleanExpression inqryDateAfter(LocalDateTime now) {
                LocalDateTime week = now.minusWeeks(1);
                return QCity.city.inqryDate.after(week);
        }

        public City findOne(Long id) {
                return em.find(City.class, id);
        }

        public Long delete(City city) {
                em.remove(city);
                return city.getId();
        }

}
