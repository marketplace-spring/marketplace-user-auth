package com.marketplace.user_auth.repository;

import com.marketplace.user_auth.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Short> {
}
