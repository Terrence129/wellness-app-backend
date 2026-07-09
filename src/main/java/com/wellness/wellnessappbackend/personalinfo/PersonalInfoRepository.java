package com.wellness.wellnessappbackend.personalinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date:
 */

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

    Optional<PersonalInfo> findByUserId(Long userId);
}
