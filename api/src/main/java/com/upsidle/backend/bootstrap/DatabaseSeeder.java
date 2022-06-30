package com.upsidle.backend.bootstrap;

import com.upsidle.backend.persistent.domain.user.Role;
import com.upsidle.backend.service.user.RoleService;
import com.upsidle.backend.service.user.UserService;
import com.upsidle.constant.ProfileTypeConstants;
import com.upsidle.enums.RoleType;
import com.upsidle.shared.util.UserUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * A convenient class to initializes and save user data on application start.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

  private final Environment environment;
  private final UserService userService;
  private final RoleService roleService;

  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    Arrays.stream(RoleType.values()).forEach(roleType -> roleService.save(new Role(roleType)));

    // only run these initial data if we are not in test mode.
    if (!Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.TEST)) {
      persistDefaultAdminUser();
    }
  }

  private void persistDefaultAdminUser() {
    var adminDto = UserUtils.createUserDto(adminEmail, adminPassword, true);
    Set<RoleType> adminRoleType = Collections.singleton(RoleType.ROLE_ADMIN);

    userService.createUser(adminDto, adminRoleType);
  }
}
