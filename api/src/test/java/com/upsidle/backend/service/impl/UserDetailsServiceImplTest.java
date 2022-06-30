package com.upsidle.backend.service.impl;

import com.upsidle.TestUtils;
import com.upsidle.backend.persistent.repository.UserRepository;
import com.upsidle.shared.util.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceImplTest extends TestUtils {

  @Mock private transient UserRepository userRepository;

  @InjectMocks private transient UserDetailsServiceImpl userDetailsService;

  private transient String email;

  @BeforeEach
  void setUp() throws Exception {
    try (var mocks = MockitoAnnotations.openMocks(this)) {
      Assertions.assertNotNull(mocks);

      email = FAKER.internet().emailAddress();
      var user = UserUtils.createUser(email, FAKER.internet().password());

      Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
      Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
    }
  }

  @Test
  void testShouldReturnNullGivenNullInput() {
    Assertions.assertNull(userDetailsService.loadUserByUsername(null));
  }

  @Test
  void testShouldReturnUserGivenAnExistingUsername() {
    var userDetails = userDetailsService.loadUserByUsername(email);
    Assertions.assertNotNull(userDetails);
    Assertions.assertEquals(email, userDetails.getUsername());
  }

  @Test
  void testShouldThrowExceptionForNonExistingUsername() {
    Assertions.assertThrows(
        UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername(FAKER.name().username()));
  }

  @Test
  void testShouldReturnUserGivenAnExistingEmail() {
    var userDetails = userDetailsService.loadUserByUsername(email);
    Assertions.assertNotNull(userDetails);
    Assertions.assertEquals(email, userDetails.getUsername());
  }
}
