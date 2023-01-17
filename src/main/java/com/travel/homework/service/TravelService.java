package com.travel.homework.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.travel.homework.domain.entity.City;
import com.travel.homework.domain.entity.Travel;
import com.travel.homework.domain.entity.Users;
import com.travel.homework.repository.CityRepository;
import com.travel.homework.repository.TravelRepository;
import com.travel.homework.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    /**
     * 여행 등록
     */
    @Transactional
    public Long planTravel(String travelname, Long cityId, Long userId, LocalDateTime startDate,
            LocalDateTime endDate) {

        // 1. Entity 조회
        City city = cityRepository.findOne(cityId);
        Users user = userRepository.findOne(userId);

        // 2. Travel 등록
        Travel travel = Travel.planTravel(travelname, city, user, startDate, endDate);

        travelRepository.save(travel);

        return travel.getId();
    }

    /*
     * 여행 수정
     */
    @Transactional
    public void updateTravel(Long id, Long city_id, String travelname, LocalDateTime startDate, LocalDateTime endDate) {

        Travel travel = travelRepository.findOne(id);
        City city = cityRepository.findOne(city_id);

        travel.setCity(city);
        travel.setStartDate(startDate);
        travel.setEndDate(endDate);

    }

    /*
     * 여행 삭제
     */
    @Transactional
    public void deleteTravel(Travel travel) {

        travelRepository.delete(travel);

    }

    /*
     * 여행 조회
     */
    public Travel findOne(Long id) {
        Travel travel = travelRepository.findOne(id);
        return travel;
    }
}
