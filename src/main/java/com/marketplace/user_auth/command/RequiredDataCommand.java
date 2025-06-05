package com.marketplace.user_auth.command;

import com.marketplace.user_auth.entity.UserStatus;
import com.marketplace.user_auth.entity.UserType;
import com.marketplace.user_auth.repository.UserStatusRepository;
import com.marketplace.user_auth.repository.UserTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1)
public class RequiredDataCommand implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;
    private final UserStatusRepository userStatusRepository;

    RequiredDataCommand(UserTypeRepository userTypeRepository, UserStatusRepository userStatusRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(UserType.Type.class.getEnumConstants()).forEach(type -> {
           if (!userTypeRepository.existsById(type.getValue())) {
               UserType userType = new UserType();
               userType.setId(type.getValue());
               userType.setName(type.name());
               userTypeRepository.save(userType);
           }
        });

        Arrays.stream(UserStatus.Status.class.getEnumConstants()).forEach(status -> {
            if (!userStatusRepository.existsById(status.getValue())) {
                UserStatus userStatus = new UserStatus();
                userStatus.setId(status.getValue());
                userStatus.setName(status.name());
                userStatusRepository.save(userStatus);
            }
        });
    }
}
