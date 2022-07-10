package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.mail.EmailService;
import com.upsidle.backend.service.security.EncryptionService;
import com.upsidle.backend.service.security.JwtService;
import com.upsidle.backend.service.user.UserService;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.constant.user.PasswordConstants;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.enums.OperationStatus;
import com.upsidle.enums.UserHistoryType;
import com.upsidle.exception.InvalidServiceRequestException;
import com.upsidle.exception.user.UserNotFoundException;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.util.UserUtils;
import com.upsidle.shared.util.core.SecurityUtils;
import com.upsidle.shared.util.core.WebUtils;
import com.upsidle.web.payload.request.PasswordResetRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Password controller is responsible for handling all password resets.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(PasswordConstants.PASSWORD_RESET_ROOT_MAPPING)
public class PasswordController {

  private final JwtService jwtService;
  private final UserService userService;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final EncryptionService encryptionService;

  @Value("${customerPortalUrl}")
  private String customerPortalUrl;

  /**
   * Generates a link to resets password. Link is then emailed to user.
   *
   * @param passwordReset the passwordReset of the user
   * @return status of the operation
   */
  @Loggable
  @SecurityRequirements
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> passwordReset(
      @RequestBody PasswordResetRequest passwordReset) {

    var userDto = userService.findByEmail(passwordReset.getEmail());
    if (Objects.nonNull(userDto)) {

      // send passwordReset to the user to verify passwordReset to complete sign-up
      // process.
      var token = jwtService.generateJwtToken(userDto.getEmail());
      userDto.setVerificationToken(token);
      userDto = userService.updateUser(userDto, UserHistoryType.PASSWORD_UPDATE_REQUESTED);

      if (Objects.nonNull(userDto)) {
        var encryptedToken = encryptionService.encrypt(token);
        var encodedToken = encryptionService.encode(encryptedToken);
        emailService.sendPasswordResetEmail(userDto, encodedToken);
      }
    } else {
      LOG.debug(UserConstants.USER_NOT_FOUND + " passwordReset: {}", passwordReset);
    }

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  /**
   * Continuation of password reset is handled by this mapping.
   *
   * @param token the token.
   * @return the view mapping for login.
   */
  @Loggable
  @SecurityRequirements
  @GetMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public ResponseEntity<String> completeSignUp(@RequestParam String token) {
    if (SecurityUtils.isAuthenticated()) {
      // if the user is already logged in, abort the process.
      LOG.debug(PasswordConstants.ACCOUNT_IN_SESSION);
      throw new IllegalArgumentException(PasswordConstants.ACCOUNT_IN_SESSION);
    }

    var decodedToken = encryptionService.decode(token);
    var decryptedToken = encryptionService.decrypt(decodedToken);
    validateToken(decryptedToken);

    var encodedToken = encryptionService.encode(token);
    var location =
        WebUtils.getUri(customerPortalUrl, PasswordConstants.PASSWORD_RESET_NEW, encodedToken);
    return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
  }

  /**
   * Processes the post request after the new password has been submitted to change the user's
   * password.
   *
   * @return the change password view name
   */
  @Loggable
  @SecurityRequirements
  @PostMapping(PasswordConstants.PASSWORD_CHANGE_PATH)
  public ResponseEntity<OperationStatus> changePassword(
      @RequestBody PasswordResetRequest passwordRequest, @RequestParam String token) {

    var decodedToken = encryptionService.decode(token);
    var decryptedToken = encryptionService.decrypt(decodedToken);
    validateToken(decryptedToken);

    var email = jwtService.getEmailFromToken(decryptedToken);
    var userDto = userService.findByEmail(email);
    // check if the password is valid.
    validatePasswords(passwordRequest, userDto);

    UserUtils.enableUser(userDto);
    userDto.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

    userDto.setVerificationToken(null);
    var updatedUserDto = userService.updateUser(userDto, UserHistoryType.PASSWORD_UPDATE);
    if (Objects.isNull(updatedUserDto)) {
      LOG.debug(PasswordConstants.PASSWORD_UPDATE_ERROR);
      throw new InvalidServiceRequestException(PasswordConstants.PASSWORD_UPDATE_ERROR);
    }
    emailService.sendPasswordResetConfirmationEmail(userDto);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  /**
   * Validates the password to ensure it's not the same as the existing one.
   *
   * @param passwordRequest the password request
   * @param userDto the user dto
   */
  private void validatePasswords(PasswordResetRequest passwordRequest, UserDto userDto) {
    if (Objects.isNull(userDto)) {
      LOG.debug(UserConstants.USER_NOT_FOUND);
      throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
    }

    // check if the password is valid.
    if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), userDto.getPassword())) {
      LOG.debug(PasswordConstants.INVALID_PASSWORD);
      throw new IllegalArgumentException(PasswordConstants.INVALID_PASSWORD);
    }

    // check if user's new password is the same as the current one
    if (passwordEncoder.matches(passwordRequest.getNewPassword(), userDto.getPassword())) {
      throw new IllegalArgumentException(PasswordConstants.SAME_PASSWORD);
    }
  }

  /**
   * Validates the token to ensure it's not expired and belongs to the user.
   *
   * @param decryptedToken the decrypted token
   */
  private void validateToken(String decryptedToken) {
    // check if the token is a valid JWT and not expired.
    if (StringUtils.isBlank(decryptedToken) || !jwtService.isValidJwtToken(decryptedToken)) {
      LOG.debug(ErrorConstants.INVALID_TOKEN);
      throw new IllegalArgumentException(ErrorConstants.INVALID_TOKEN);
    }

    // check if the token belongs to the user.
    var email = jwtService.getEmailFromToken(decryptedToken);
    if (StringUtils.isBlank(email) || !userService.isValidEmailAndToken(email, decryptedToken)) {
      LOG.debug(ErrorConstants.UNAUTHORIZED_ACCESS);
      throw new IllegalArgumentException(ErrorConstants.UNAUTHORIZED_ACCESS);
    }
  }
}
