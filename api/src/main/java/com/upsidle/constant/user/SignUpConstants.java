package com.upsidle.constant.user;

import com.upsidle.constant.ErrorConstants;

/**
 * This class holds all constants used in controller implementations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class SignUpConstants {

  /** URL Mapping Constants. */
  public static final String SIGN_UP_MAPPING = "/sign-up";

  public static final String ACCOUNT_EXISTS = "Account already exist!";

  public static final String EMAIL_VERIFY_MAPPING = "/verify-email";

  private SignUpConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
