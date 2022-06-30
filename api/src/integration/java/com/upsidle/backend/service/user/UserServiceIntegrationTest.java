package com.upsidle.backend.service.user;

import com.upsidle.IntegrationTestUtils;
import com.upsidle.backend.persistent.domain.user.Role;
import com.upsidle.backend.service.impl.UserDetailsBuilder;
import com.upsidle.enums.RoleType;
import com.upsidle.enums.UserHistoryType;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.util.UserUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class UserServiceIntegrationTest extends IntegrationTestUtils {

  /**
   * Test attempts to create a user, verify that user has been created successfully by checking
   * assigned id.
   */
  @Test
  void createUserWithoutSpecifiedRoles() {
    var email = FAKER.internet().emailAddress();
    var userDto = UserUtils.createUserDto(email);
    var persistedUserDto = userService.createUser(userDto);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(persistedUserDto);
          Assertions.assertNotNull(persistedUserDto.getId());
          Assertions.assertEquals(userDto, persistedUserDto);

          // assert that the user now has a new USER role assigned after creation.
          Assertions.assertFalse(
              persistedUserDto.getUserRoles().stream()
                  .filter(userRole -> Objects.nonNull(userRole.getRole()))
                  .filter(userRole -> userRole.getRole().equals(new Role(RoleType.ROLE_USER)))
                  .collect(Collectors.toSet())
                  .isEmpty());
        });
  }

  /** Creating a user who exists and not enabled should return the existing user. */
  @Test
  void createUserAlreadyExistingAndNotEnabled() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    // create another user using the same details from the first user "userDto"
    var existingUser = createAndAssertUser(userDto);

    // Assert that the existing user is returned as is and not enabled.
    Assertions.assertEquals(userDto, existingUser);
  }

  /** Creating a user who exists and enabled should return null. */
  @Test
  void createUserAlreadyExistingAndEnabled() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, true);

    // since the user is enabled, create another user using the same details from the first user
    // should return null as the user already exists.
    var existingUser = persistUser(true, userDto);

    Assertions.assertNull(existingUser);
  }

  @Test
  void getUserById() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    var storedUser = userService.findById(userDto.getId());
    Assertions.assertEquals(userDto, storedUser);
  }

  @Test
  void getUserByIdNotExisting() {

    var storedUser = userService.findById(RandomUtils.nextLong());
    Assertions.assertNull(storedUser);
  }

  @Test
  void getUserByPublicId() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    var storedUser = userService.findByPublicId(userDto.getPublicId());
    Assertions.assertEquals(userDto, storedUser);
  }

  @Test
  void getUserByPublicIdNotExisting(TestInfo testInfo) {

    var storedUser = userService.findByPublicId(testInfo.getDisplayName());
    Assertions.assertNull(storedUser);
  }

  @Test
  void getUserByEmail() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    var userByEmail = userService.findByEmail(userDto.getEmail());
    Assertions.assertEquals(userDto, userByEmail);
  }

  @Test
  void getUserByEmailNotExisting(TestInfo testInfo) {
    var userByEmail = userService.findByEmail(testInfo.getDisplayName());
    Assertions.assertNull(userByEmail);
  }

  @Test
  void getUserHistories() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    var userHistoryDtos = UserUtils.convertToUserHistoryDto(userDto.getUserHistories());

    Assertions.assertFalse(userHistoryDtos.isEmpty());
    Assertions.assertEquals(1, userHistoryDtos.size());
    Assertions.assertEquals(userHistoryDtos.get(0).getUserHistoryType(), UserHistoryType.CREATED);
  }

  @Test
  void findAllNotEnabledAfterCreationDays() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    List<UserDto> users = userService.findAllNotEnabledAfterAllowedDays();
    // User was just created and should not be returned to be deleted.
    Assertions.assertFalse(users.contains(userDto));
  }

  @Test
  void getUserDetails() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    var userDetails = userService.getUserDetails(userDto.getEmail());
    Assertions.assertTrue(userDetails instanceof UserDetailsBuilder);
  }

  @Test
  void updateUser() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);
    var previousName = userDto.getName();
    userDto.setName(FAKER.name().fullName());

    var updatedUserDto = userService.updateUser(userDto, UserHistoryType.PROFILE_UPDATE);
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertNotEquals(previousName, updatedUserDto.getName());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());
    Assertions.assertFalse(updatedUserDto.getUserRoles().isEmpty());
    Assertions.assertEquals(updatedUserDto.getUserRoles().size(), userDto.getUserRoles().size());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void enableUser() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, false);

    // User should not be enabled after creation.
    Assertions.assertFalse(userDto.isEnabled());

    // Enable the user.
    var updatedUserDto = userService.enableUser(userDto.getPublicId());
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertTrue(updatedUserDto.isEnabled());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void enableUserNotExistingDoesNothing(TestInfo testInfo) {
    Assertions.assertNull(userService.enableUser(testInfo.getDisplayName()));
  }

  @Test
  void disableUser() {
    var email = FAKER.internet().emailAddress();
    var userDto = createAndAssertUser(email, true);

    // User should be enabled after creation.
    Assertions.assertTrue(userDto.isEnabled());

    // Disable the user.
    var updatedUserDto = userService.disableUser(userDto.getPublicId());
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertFalse(updatedUserDto.isEnabled());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void isValidPublicIdAndTokenWithValidToken() {
    var userDto = createAndAssertUser(UserUtils.createUserDto(false));
    var token = jwtService.generateJwtToken(userDto.getEmail());
    Assertions.assertFalse(userService.isValidEmailAndToken(userDto.getEmail(), token));

    userDto.setVerificationToken(token);
    userService.saveOrUpdate(UserUtils.convertToUser(userDto), true);

    Assertions.assertTrue(userService.isValidEmailAndToken(userDto.getEmail(), token));
  }

  @Test
  void isValidPublicIdAndTokenWithInvalidToken(TestInfo testInfo) {
    Assertions.assertFalse(
        userService.isValidEmailAndToken(testInfo.getDisplayName(), testInfo.getDisplayName()));
  }

  @Test
  void isValidPublicIdAndTokenWithNullTokenThrowsException(TestInfo testInfo) {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> userService.isValidEmailAndToken(null, testInfo.getDisplayName()));
  }

  @Test
  void disableUserNotExistingDoesNothing(TestInfo testInfo) {
    Assertions.assertNull(userService.disableUser(testInfo.getDisplayName()));
  }

  @Test
  void deleteUser() {
    var userDto = createAndAssertUser(UserUtils.createUserDto(false));
    Assertions.assertTrue(userService.existsByEmail(userDto.getEmail()));

    userService.deleteUser(userDto.getPublicId());
    Assertions.assertFalse(userService.existsByEmail(userDto.getEmail()));
  }
}
