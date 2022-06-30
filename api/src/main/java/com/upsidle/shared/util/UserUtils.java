package com.upsidle.shared.util;

import com.upsidle.backend.persistent.domain.user.Role;
import com.upsidle.backend.persistent.domain.user.User;
import com.upsidle.backend.persistent.domain.user.UserHistory;
import com.upsidle.backend.persistent.domain.user.UserRole;
import com.upsidle.backend.service.impl.UserDetailsBuilder;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.constant.user.ProfileConstants;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.enums.RoleType;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.dto.UserHistoryDto;
import com.upsidle.shared.dto.mapper.UserDtoMapper;
import com.upsidle.shared.dto.mapper.UserHistoryDtoMapper;
import com.upsidle.shared.util.core.ValidationUtils;
import com.upsidle.web.payload.request.SignUpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * User utility class that holds methods used across application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
public final class UserUtils {

  /** The Constant FAKER. */
  private static final Faker FAKER = new Faker();

  /** Minimum password length for the password generation. */
  private static final int PASSWORD_MIN_LENGTH = 4;

  /** Maximum password length for the password generation. */
  public static final int PASSWORD_MAX_LENGTH = 15;

  private UserUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Create a user.
   *
   * @return a user
   */
  public static User createUser() {
    return createUser(FAKER.internet().emailAddress());
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the user
   */
  public static User createUser(final boolean enabled) {
    return createUser(
        FAKER.internet().emailAddress(),
        FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
        enabled);
  }

  /**
   * Create a user with some flexibility.
   *
   * @param email email used to create user.
   * @param roleType the role type
   * @return a user
   */
  public static User createUser(String email, RoleType roleType) {
    var user = createUser(email);
    user.getUserRoles().add(new UserRole(user, new Role(roleType)));
    return user;
  }

  /**
   * Create a user with some flexibility.
   *
   * @param email email used to create user.
   * @return a user
   */
  public static User createUser(String email) {
    return createUser(email, FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
  }

  /**
   * Create a user with some flexibility.
   *
   * @param email email used to create user
   * @param password password used to create user.
   * @return a user
   */
  public static User createUser(String email, String password) {
    return createUser(email, password, false);
  }

  /**
   * Create user with some flexibility.
   *
   * @param password password used to create user.
   * @param email email used to create user.
   * @param enabled boolean value used to evaluate if user enabled.
   * @return a user
   */
  public static User createUser(String email, String password, boolean enabled) {
    var user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setPhone(FAKER.phoneNumber().cellPhone());
    user.setName(FAKER.name().nameWithMiddle());

    if (enabled) {
      user.setEnabled(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
    }
    return user;
  }

  /**
   * Create a test user with flexibility.
   *
   * @param email the email
   * @return the userDto
   */
  public static UserDto createUserDto(final String email) {
    return UserUtils.convertToUserDto(createUser(email));
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the userDto
   */
  public static UserDto createUserDto(final boolean enabled) {
    return createUserDto(FAKER.internet().emailAddress(), enabled);
  }

  /**
   * Create a test user with flexibility.
   *
   * @param email the email
   * @param enabled if the user should be enabled to authenticate
   * @return the userDto
   */
  public static UserDto createUserDto(final String email, boolean enabled) {
    var userDto = UserUtils.convertToUserDto(createUser(email));
    if (enabled) {
      enableUser(userDto);
    }
    return userDto;
  }

  /**
   * Create user with some flexibility.
   *
   * @param email email used to create user.
   * @param password password used to create user.
   * @param enabled boolean value used to evaluate if user enabled.
   * @return a userDto
   */
  public static UserDto createUserDto(String email, String password, boolean enabled) {
    var user = createUser(email, password, enabled);

    return UserUtils.convertToUserDto(user);
  }

  /**
   * Transfers data from signUpRequest to transfer object.
   *
   * @param signUpRequest signUpRequest
   * @return user dto
   */
  public static UserDto convertToUserDto(final SignUpRequest signUpRequest) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(signUpRequest);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDto;
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param user stored user
   * @return user dto
   */
  public static UserDto convertToUserDto(final User user) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(user);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDto;
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param users stored users
   * @return user dto
   */
  public static List<UserDto> convertToUserDto(final List<User> users) {
    var userDtoList = UserDtoMapper.MAPPER.toUserDto(users);
    Validate.notNull(userDtoList, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDtoList;
  }

  /**
   * Transfers data from userDetails to dto object.
   *
   * @param userDetailsBuilder stored user details
   * @return user dto
   */
  public static UserDto convertToUserDto(UserDetailsBuilder userDetailsBuilder) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(userDetailsBuilder);
    Validate.notNull(userDetailsBuilder, "userDetailsBuilder cannot be null");
    return userDto;
  }

  /**
   * Transfers data from transfer object to entity.
   *
   * @param userDto the userDto
   * @return user
   */
  public static User convertToUser(final UserDto userDto) {
    var user = UserDtoMapper.MAPPER.toUser(userDto);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return user;
  }

  /**
   * Transfers data from entity to returnable object.
   *
   * @param userHistories stored userHistories details
   * @return userHistories dto
   */
  public static List<UserHistoryDto> convertToUserHistoryDto(final Set<UserHistory> userHistories) {
    ValidationUtils.validateInputs(userHistories);

    return UserHistoryDtoMapper.MAPPER.toUserHistoryDto(userHistories);
  }

  /**
   * Enables and unlocks a user.
   *
   * @param userDto the userDto
   */
  public static void enableUser(final UserDto userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    userDto.setEnabled(true);
    userDto.setAccountNonExpired(true);
    userDto.setAccountNonLocked(true);
    userDto.setCredentialsNonExpired(true);
  }

  /**
   * Retrieves the roles from the userRoles.
   *
   * @param userRoles the userRoles
   * @return set of the roles as strings
   */
  public static List<String> getRoles(Set<UserRole> userRoles) {
    List<String> roles = new ArrayList<>();

    for (UserRole userRole : userRoles) {
      if (Objects.nonNull(userRole.getRole())) {
        roles.add(userRole.getRole().getName());
      }
    }
    return roles;
  }

  /**
   * Returns the role with the highest precedence if user has multiple roles.
   *
   * @param user the user
   * @return the role
   */
  public static String getTopmostRole(User user) {
    ValidationUtils.validateInputs(user);

    if (Objects.isNull(user.getUserRoles())) {
      return null;
    }

    List<String> roles = getRoles(user.getUserRoles());

    if (roles.contains(RoleType.ROLE_ADMIN.getName())) {
      return RoleType.ROLE_ADMIN.getName();
    }

    return RoleType.ROLE_USER.getName();
  }

  /**
   * Returns the user profile or random image if not found.
   *
   * @param user the user
   * @return profile image
   */
  public static String getUserProfileImage(User user) {
    if (StringUtils.isBlank(user.getProfileImage())) {
      return ProfileConstants.PIC_SUM_PHOTOS_150_RANDOM;
    }

    return user.getProfileImage();
  }
}
