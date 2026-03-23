package com.CardMaster.modules.iam.repository;

import com.CardMaster.modules.iam.entity.User;
import com.CardMaster.modules.iam.enums.UserEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
