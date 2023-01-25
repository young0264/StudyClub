package com.studyclub.modules.zone;

import org.springframework.data.jpa.repository.JpaRepository;

//@Transactional(readOnly = true)
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Zone findByCityAndProvince(String cityName, String provinceName);
}
