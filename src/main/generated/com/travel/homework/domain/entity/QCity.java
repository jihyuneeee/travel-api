package com.travel.homework.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCity is a Querydsl query type for City
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCity extends EntityPathBase<City> {

    private static final long serialVersionUID = -1217149053L;

    public static final QCity city = new QCity("city");

    public final StringPath cityname = createString("cityname");

    public final NumberPath<Integer> enable = createNumber("enable", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> inqryDate = createDateTime("inqryDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final ListPath<Travel, QTravel> travels = this.<Travel, QTravel>createList("travels", Travel.class, QTravel.class, PathInits.DIRECT2);

    public QCity(String variable) {
        super(City.class, forVariable(variable));
    }

    public QCity(Path<? extends City> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCity(PathMetadata metadata) {
        super(City.class, metadata);
    }

}

