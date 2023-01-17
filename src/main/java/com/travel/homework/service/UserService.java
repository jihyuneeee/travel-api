package com.travel.homework.service;

import org.springframework.stereotype.Service;
import com.travel.homework.domain.entity.Users;
import com.travel.homework.repository.UserRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 등록
     */
    @Transactional
    public Long registerUser(Users user) {

        userRepository.save(user);
        return user.getId();

    }

    /**
     * 중복 회원 검색
     */
    public boolean validateDuplicateUser(String username) {

        List<Users> findUsers = userRepository.findByUsername(username);

        if (!findUsers.isEmpty()) {
            return false;
        }

        return true;

    }
}
