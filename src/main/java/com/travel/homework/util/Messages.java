package com.travel.homework.util;

import org.springframework.stereotype.Component;

@Component
public class Messages {

    public static String USER_EXISIT = "이미 존재하는 회원입니다.";
    public static String USER_CREATED = "회원등록이 완료되었습니다.";

    public static String CITY_CREATED = "도시 등록이 완료되었습니다.";
    public static String CITY_EXISIT = "해당 도시는 이미 등록이 되어있습니다.";
    public static String CITY_UPATED = "해당 도시 정보가 변경되었습니다.";
    public static String CITY_CANT_FIND = "도시를 찾을 수 없습니다.";
    public static String CITY_FIND = "도시 정보가 존재합니다.";
    public static String CITY_USED = "해당 도시를 여행하고 있습니다.";
    public static String CITY_DELETED = "도시가 삭제되었습니다.";

    public static String TRAVEL_ENDDATE_INVALID = "종료일자가 유효하지 않습니다.";
    public static String TRAVEL_CREATED = "여행이 등록되었습니다.";
    public static String TRAVEL_UPDATED = "여행 정보가 수정되었습니다.";
    public static String TRAVEL_FIND = "여행 정보가 존재합니다.";
    public static String TRAVEL_CANT_FIND = "조회할 여행이 없습니다.";
    public static String TRAVEL_DELETED = "여행이 삭제되었습니다.";

}
