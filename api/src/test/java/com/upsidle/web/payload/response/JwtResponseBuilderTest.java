package com.upsidle.web.payload.response;

import com.upsidle.TestUtils;
import com.upsidle.backend.persistent.domain.user.Role;
import com.upsidle.backend.persistent.domain.user.UserRole;
import com.upsidle.backend.service.impl.UserDetailsBuilder;
import com.upsidle.constant.SecurityConstants;
import com.upsidle.enums.RoleType;
import com.upsidle.shared.util.UserUtils;
import com.upsidle.shared.util.core.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test JwtResponse builder class.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
class JwtResponseBuilderTest extends TestUtils {

  private transient UserDetailsBuilder userDetailsBuilder;
  private static final String JWT_TOKEN =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
          + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6InRlc3QgdXNlciIsImlhdCI6MTUxNjIzOTAyMn0."
          + "WUwx8SdGax2poWdgIbL3mMLamAVYb6fF1t87jSyGh94";

  @BeforeAll
  static void beforeAll() {
    SecurityUtils.clearAuthentication();
  }

  @BeforeEach
  void setUp() {
    // create user from javaFaker
    var user = UserUtils.createUser();

    // create UserDetails from user
    userDetailsBuilder = UserDetailsBuilder.buildUserDetails(user);
  }

  @Test
  void shouldReturnEmptyJwtResponseWithNoUserDetails() {
    JwtResponseBuilder jwtResponse = JwtResponseBuilder.buildJwtResponse(JWT_TOKEN);
    Assertions.assertNotNull(jwtResponse);
    Assertions.assertTrue(StringUtils.isBlank(jwtResponse.getAccessToken()));
  }

  @Test
  void shouldReturnJwtResponseWithMatchingToken() {
    JwtResponseBuilder jwtResponse =
        JwtResponseBuilder.buildJwtResponse(JWT_TOKEN, userDetailsBuilder);
    Assertions.assertNotNull(jwtResponse);
    Assertions.assertEquals(JWT_TOKEN, jwtResponse.getAccessToken());
  }

  @Test
  void givenJwtTokenAndUserDetailsShouldBuildSuccessfully() {
    var email = FAKER.internet().emailAddress();
    var user = UserUtils.createUser(email);
    user.addUserRole(user, new Role(RoleType.ROLE_USER));

    var userDetails = UserDetailsBuilder.buildUserDetails(user);
    var jwtResponse = JwtResponseBuilder.buildJwtResponse(JWT_TOKEN, userDetails);

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(JWT_TOKEN, jwtResponse.getAccessToken());
          Assertions.assertEquals(user.getName(), jwtResponse.getName());
          Assertions.assertEquals(user.getEmail(), jwtResponse.getEmail());
          Assertions.assertEquals(SecurityConstants.BEARER, jwtResponse.getType());

          Assertions.assertEquals(user.getUserRoles().size(), jwtResponse.getRoles().size());
          for (UserRole userRole : user.getUserRoles()) {
            Assertions.assertTrue(jwtResponse.getRoles().contains(userRole.getRole().getName()));
          }
        });
  }
}
