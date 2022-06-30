package com.upsidle.backend.persistent.domain.user;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.upsidle.TestUtils;
import com.upsidle.enums.RoleType;
import com.upsidle.shared.util.UserUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void equalsContract() {
    User client = UserUtils.createUser();
    User admin = UserUtils.createUser();

    EqualsVerifier.forClass(User.class)
        .withRedefinedSuperclass()
        .withPrefabValues(User.class, client, admin)
        .withOnlyTheseFields(TestUtils.getUserEqualsFields().toArray(new String[0]))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(User.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password", "userRoles", "userHistories")
        .verify();
  }

  @Test
  void testAddUserRole() {
    User user = UserUtils.createUser();
    user.addUserRole(user, new Role(RoleType.ROLE_USER));

    Assertions.assertFalse(user.getUserRoles().isEmpty());
  }

  @Test
  void testRemoveUserRole() {
    User user = UserUtils.createUser();
    user.addUserRole(user, new Role(RoleType.ROLE_USER));
    Assertions.assertFalse(user.getUserRoles().isEmpty());

    user.removeUserRole(user, new Role(RoleType.ROLE_USER));
    Assertions.assertTrue(user.getUserRoles().isEmpty());
  }
}
