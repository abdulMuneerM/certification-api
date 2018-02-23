package com.domain.certification.api.util;

import com.domain.certification.api.config.ConfigProperties;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.service.UserService;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import com.domain.certification.api.util.enumerator.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartRunner implements ApplicationRunner {

    private final UserService userService;
    private final ConfigProperties configProperties;
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartRunner.class);

    public ApplicationStartRunner(UserService userService, ConfigProperties configProperties) {
        this.userService = userService;
        this.configProperties = configProperties;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOG.info("Application runner starting.....");
        try {
            userService.findByEmail(configProperties.getAdminEmail());
        } catch (EntityNotFoundException ignored) {
            UserRequestDTO userRequestDTO = new UserRequestDTO();
            userRequestDTO.setEmail(configProperties.getAdminEmail());
            userRequestDTO.setPassword(configProperties.getAdminPassword());
            userRequestDTO.setName("Admin");
            userRequestDTO.setRoleType(RoleType.ADMIN);
            userService.save(userRequestDTO);
            LOG.info("Admin user created successfully.");
        }
    }
}
