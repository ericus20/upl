package com.upsidle.constant.product;

import com.upsidle.constant.ErrorConstants;

/**
 * Product constant provides details about product.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class CartConstants {

  public static final int DAYS_TO_ALLOW_ACCOUNT_ACTIVATION = 30;
  public static final String API_V1_CART_ROOT_URL = "/api/v1/carts";

  public static final String USER_MODEL_KEY = "user";
  public static final String EMAIL = "email";
  public static final String PASSWORD = "password";
  public static final String USERNAME = "username";
  public static final String TITLE = "title";
  public static final String PUBLIC_ID = "publicId";

  public static final String USER_PERSISTED_SUCCESSFULLY = "User successfully persisted {}";

  /** Username cannot be left blank. */
  public static final String BLANK_TITLE = "Title cannot be blank";

  public static final String PRODUCT_MUST_NOT_BE_NULL = "Product must not be null";
  public static final String USER_DTO_MUST_NOT_BE_NULL = "UserDto must not be null";

  /** Username should be at least 3 and at most 50 characters. */
  public static final String USERNAME_SIZE =
      "Username should be at least 3 and at most 50 characters";

  /** Email cannot be blank. */
  public static final String BLANK_EMAIL = "Email cannot be blank";

  public static final String BLANK_NAME = "Name cannot be blank";

  public static final String BLANK_PUBLIC_ID = "PublicId cannot be left blank";

  /** A valid email format is required. */
  public static final String INVALID_EMAIL = "A valid email format is required";

  /** Password cannot be left blank. */
  public static final String BLANK_PASSWORD = "Password cannot be left blank";

  public static final String PASSWORD_SIZE = "Password should be at least 4 characters";

  public static final String COULD_NOT_CREATE_USER = "Could not create user";
  public static final String COULD_NOT_VERIFY_USER = "Could not create user";
  public static final String USER_ALREADY_EXIST = "Email {} already exist and nothing will be done";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String EMAIL_EXITS = "Email already exist";
  public static final String USER_EXIST_BUT_NOT_ENABLED =
      "Email {} exists but not enabled. Returning user {}";
  public static final String USER_DETAILS_DEBUG_MESSAGE = "User details {}";
  public static final String USER_ID_MUST_NOT_BE_NULL = "User Id must not be null";
  public static final String USER_DISABLED_MESSAGE = "User is disabled";
  public static final String USER_LOCKED_MESSAGE = "User is locked";
  public static final String USER_EXPIRED_MESSAGE = "User is expired";
  public static final String CATEGORY_NOT_FOUND = "Category not found";
  public static final String CATEGORY_MUST_NOT_BE_NULL = "Product category must not be null";

  private CartConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
