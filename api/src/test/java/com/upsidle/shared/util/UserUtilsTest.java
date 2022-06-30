package com.upsidle.shared.util;

import com.upsidle.TestUtils;
import com.upsidle.backend.persistent.domain.user.User;
import com.upsidle.enums.RoleType;
import com.upsidle.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;

class UserUtilsTest extends TestUtils {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(UserUtils.class));
  }

  @Test
  void createUserWithNoArgument() {
    User user = UserUtils.createUser();
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user.getPassword());
          Assertions.assertNotNull(user.getEmail());
        });
  }

  @Test
  void createUserWithAnArgument(TestInfo testInfo) {
    User user = UserUtils.createUser(testInfo.getDisplayName());
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user.getPassword());
          Assertions.assertNotNull(user.getEmail());

          Assertions.assertEquals(testInfo.getDisplayName(), user.getEmail());
        });
  }

  @Test
  void createUserWithFourParameters(TestInfo testInfo) {

    UserDto userDto =
        UserUtils.createUserDto(
            FAKER.internet().emailAddress(), testInfo.getDisplayName(), Boolean.TRUE);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userDto.getPassword());
          Assertions.assertNotNull(userDto.getEmail());
          Assertions.assertTrue(userDto.isEnabled());
        });
  }

  @Test
  void convertToUserDto() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(user.getEmail(), userDto.getEmail());
          Assertions.assertNotNull(userDto.getPassword());
        });
  }

  @Test
  void convertToUserDtoWithNullCollections() {
    User user = UserUtils.createUser();
    user.setUserRoles(null);
    user.setUserHistories(null);
    var userDto = UserUtils.convertToUserDto(user);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userDto.getUserRoles());
          Assertions.assertNotNull(userDto.getUserHistories());
        });
  }

  @Test
  void convertToUser() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);

    var userFromUserDto = UserUtils.convertToUser(userDto);
    Assertions.assertEquals(user, userFromUserDto);
  }

  @Test
  void convertToUserWithNullCollections() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);
    userDto.setUserRoles(null);
    userDto.setUserHistories(null);

    var userFromUserDto = UserUtils.convertToUser(userDto);
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userFromUserDto.getUserRoles());
          Assertions.assertNotNull(userFromUserDto.getUserHistories());
        });
  }

  @Test
  void shouldThrowExceptionWhenUserDtoInputIsNull() {
    Assertions.assertThrows(NullPointerException.class, () -> UserUtils.convertToUser(null));
  }

  @Test
  void retrieveRolesFromUserRole(TestInfo testInfo) {
    User user = UserUtils.createUser(testInfo.getDisplayName(), RoleType.ROLE_ADMIN);
    UserUtils.getRoles(user.getUserRoles());

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user);
          Assertions.assertNotNull(user.getUserRoles());
        });
  }
}
