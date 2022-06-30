package com.upsidle.backend.service.impl;

import com.upsidle.backend.persistent.domain.user.User;
import com.upsidle.constant.user.UserConstants;
import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetailsBuilder builds the userDetails to be used by the application security context.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserDetailsBuilder implements UserDetails {
  @Serial private static final long serialVersionUID = -8755873164632782035L;

  private Long id;
  @EqualsAndHashCode.Include private String email;
  @EqualsAndHashCode.Include private String publicId;

  private String name;
  private String password;
  private String phone;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Builds userDetails object from the specified user.
   *
   * @param user the user
   * @throws NullPointerException if the user is null
   * @return the userDetails
   */
  public static UserDetailsBuilder buildUserDetails(User user) {
    Validate.notNull(user, UserConstants.USER_MUST_NOT_BE_NULL);

    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities = new HashSet<>();
    user.getUserRoles()
        .forEach(
            userRole -> {
              if (Objects.nonNull(userRole.getRole())) {
                authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
              }
            });

    return UserDetailsBuilder.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .publicId(user.getPublicId())
        .password(user.getPassword())
        .publicId(user.getPublicId())
        .enabled(user.isEnabled())
        .accountNonExpired(user.isAccountNonExpired())
        .accountNonLocked(user.isAccountNonLocked())
        .credentialsNonExpired(user.isCredentialsNonExpired())
        .authorities(authorities)
        .build();
  }
}
