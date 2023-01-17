package com.travel.homework.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.travel.homework.domain.entity.City;
import com.travel.homework.domain.entity.Travel;
import com.travel.homework.repository.CityRepository;
import com.travel.homework.repository.TravelRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    private final TravelRepository travelRepository;

    /**
     * 도시 등록
     */
    @Transactional
    public Long registerCity(City city) {

        cityRepository.save(city);
        return city.getId();
    }

    /**
     * 도시 수정
     */
    @Transactional
    public Long updateCity(Long id, String name) {
        City city = cityRepository.findOne(id);
        city.setCityname(name);
        return city.getId();
    }

    /*
     * 도시 조회
     */
    public City findOne(Long id) {

        City city = cityRepository.findOne(id);
        return city;

    }

    /*
     * 도시 조회 + 조회 시간 업데이트
     */
    @Transactional
    public City findCity(Long id) {

        City city = cityRepository.findOne(id);

        // inqryDate 업데이트
        LocalDateTime now = LocalDateTime.now();
        city.setInqryDate(now);

        return city;

    }

    /*
     * 도시, 여행 조회
     */
    public List<City> findCityJoinTravel(Long id) {
        List<City> travel = cityRepository.findById(id);
        return travel;
    }

    /*
     * 도시 삭제
     */
    @Transactional
    public void deleteCity(Long id) {

        City city = cityRepository.findOne(id);
        cityRepository.delete(city);

    }

    /**
     * 여행 중인 도시
     * 
     */
    public List<Travel> findTravelingCityWithUserId(Long id) {

        LocalDateTime now = LocalDateTime.now();
        List<Travel> travel = travelRepository.findByUserId(id, now);

        return travel;

    }

    /**
     * 여행 중 도시 제외하고, 조회
     * 
     */
    public List<City> findOtherCityWithUserId(Long id) {

        LocalDateTime now = LocalDateTime.now();

        List<City> city = cityRepository.findByUserId(id, now);

        return city;
    }

    /**
     * 중복 도시 검색
     */
    public boolean validateDuplicateCity(String cityname) {

        List<City> findCities = cityRepository.findByCityname(cityname);

        if (!findCities.isEmpty()) {
            return false;
        }

        return true;

    }

}
