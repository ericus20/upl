package com.upsidle.backend.service.impl;

import com.upsidle.backend.persistent.repository.UserRepository;
import com.upsidle.constant.CacheConstants;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of service used to query user details during login.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Locates the user based on the email. In the actual implementation, the search may be
   * case-sensitive, or case-insensitive depending on how the implementation instance is configured.
   * In this case, the <code>UserDetails</code> object that comes back may have a email that is of a
   * different case than what was actually requested..
   *
   * @param email the email identifying the user whose data is required.
   * @return a fully populated user record (never <code>null</code>)
   * @throws UsernameNotFoundException if the user could not be found or the user has no
   *     GrantedAuthority
   */
  @Override
  @Cacheable(key = "{ #root.methodName, #email }", value = CacheConstants.USER_DETAILS)
  public UserDetails loadUserByUsername(final String email) {
    // Ensure that email is not empty or null.
    if (StringUtils.isNotBlank(email)) {
      var storedUser = userRepository.findByEmail(email);
      if (Objects.isNull(storedUser)) {
        LOG.warn("No record found for storedUser with email {}", email);
        throw new UsernameNotFoundException("User with email " + email + " not found");
      }
      return UserDetailsBuilder.buildUserDetails(storedUser);
    }
    return null;
  }
}
