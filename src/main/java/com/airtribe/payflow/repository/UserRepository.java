package com.airtribe.payflow.repository;

import com.airtribe.payflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Derived query method - JPA parses "findBy" + "UPIId"
    Optional<User> findByUpiId(String upiId);

    //JPQL query - find users with balance above threshold
    @Query("SELECT u FROM User u WHERE u.balance > :minBalance")
    List<User> findUsersWithBalanceAbove(@Param("minBalance") Double minBalance);
}
