package com.upsidle.shared.dto;

import com.upsidle.backend.persistent.domain.user.UserHistory;
import com.upsidle.backend.persistent.domain.user.UserRole;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.shared.dto.product.CartItemDto;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The UserDto transfers user details from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -6342630857637389028L;

  @EqualsAndHashCode.Include
  @NotBlank(message = UserConstants.BLANK_NAME)
  private String name;

  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  private String password;

  @EqualsAndHashCode.Include
  @NotBlank(message = UserConstants.BLANK_EMAIL)
  @Email(message = UserConstants.INVALID_EMAIL)
  private String email;

  private String role;
  private String phone;
  private String profileImage;

  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private String verificationToken;

  @ToString.Exclude private Set<UserRole> userRoles = new HashSet<>();

  @ToString.Exclude private Set<UserHistory> userHistories = new HashSet<>();

  @ToString.Exclude private Set<CartItemDto> cartItems = new HashSet<>();
}
