package com.smita.dairy.farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByFarmerCode(String farmerCode);

    Optional<Farmer> findByMobile(String mobile);

    boolean existsByFarmerCode(String farmerCode);

    boolean existsByMobile(String mobile);

}