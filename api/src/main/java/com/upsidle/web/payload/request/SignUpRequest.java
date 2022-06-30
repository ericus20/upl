package com.upsidle.web.payload.request;

import com.upsidle.constant.user.UserConstants;
import com.upsidle.enums.RoleType;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class models the format of the signUp request allowed through the controller endpoints.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class SignUpRequest {

  @NotBlank(message = UserConstants.BLANK_NAME)
  private String name;

  @Size(max = 60)
  @EqualsAndHashCode.Include
  @Email(message = UserConstants.INVALID_EMAIL)
  @NotBlank(message = UserConstants.BLANK_EMAIL)
  private String email;

  @ToString.Exclude
  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, max = 15, message = UserConstants.PASSWORD_SIZE)
  private String password;

  private Set<RoleType> roles = new HashSet<>();
}
