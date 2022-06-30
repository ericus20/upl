package com.upsidle.web.payload.request;

import com.upsidle.constant.user.UserConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the login request accepted.
 *
 * @author Stephen Boakye
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LoginRequest {

  @NotBlank(message = UserConstants.BLANK_EMAIL)
  @Email(message = UserConstants.INVALID_EMAIL)
  private String email;

  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, message = UserConstants.PASSWORD_SIZE)
  private String password;
}
