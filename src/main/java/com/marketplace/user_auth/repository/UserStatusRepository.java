package com.marketplace.user_auth.repository;

import com.marketplace.user_auth.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Short> {
}
