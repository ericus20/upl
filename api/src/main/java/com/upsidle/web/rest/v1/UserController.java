package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.mail.EmailService;
import com.upsidle.backend.service.security.EncryptionService;
import com.upsidle.backend.service.security.JwtService;
import com.upsidle.backend.service.user.UserService;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.constant.SecurityConstants;
import com.upsidle.constant.user.SignUpConstants;
import com.upsidle.constant.user.UserConstants;
import com.upsidle.enums.OperationStatus;
import com.upsidle.enums.UserHistoryType;
import com.upsidle.exception.InvalidServiceRequestException;
import com.upsidle.exception.user.InvalidTokenException;
import com.upsidle.exception.user.UserAlreadyExistsException;
import com.upsidle.shared.dto.UserDto;
import com.upsidle.shared.util.UserUtils;
import com.upsidle.shared.util.core.SecurityUtils;
import com.upsidle.web.payload.request.SignUpRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * This class handles all rest calls for users.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConstants.API_V1_USERS_ROOT_URL)
public class UserController {

  private final JwtService jwtService;
  private final UserService userService;
  private final EmailService emailService;
  private final EncryptionService encryptionService;

  @Value("${customerPortalUrl}")
  private String customerPortalUrl;

  /**
   * Enables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PutMapping(value = "/{publicId}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> enableUser(@PathVariable String publicId) {
    var userDto = userService.enableUser(publicId);

    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }

  /**
   * Disables the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @PutMapping(value = "/{publicId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> disableUser(@PathVariable String publicId) {
    var userDto = userService.disableUser(publicId);

    return ResponseEntity.ok(
        Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
  }

  /**
   * Deletes the user associated with the publicId.
   *
   * @param publicId the publicId
   * @return if the operation is success
   */
  @DeleteMapping(value = "/{publicId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> deleteUser(@PathVariable String publicId) {
    userService.deleteUser(publicId);

    return ResponseEntity.ok(OperationStatus.SUCCESS);
  }

  @Loggable
  @SecurityRequirements
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> register(@RequestBody @Valid SignUpRequest user) {
    var userDto = UserUtils.convertToUserDto(user);
    userDto.setPublicId(UUID.randomUUID().toString());

    if (userService.existsByEmailAndEnabled(user.getEmail())) {
      LOG.warn(UserConstants.EMAIL_EXITS);
      throw new UserAlreadyExistsException(UserConstants.EMAIL_EXITS);
    }
    var verificationToken = jwtService.generateJwtToken(userDto.getEmail());
    userDto.setVerificationToken(verificationToken);

    var savedUserDto = userService.createUser(userDto, user.getRoles());
    if (Objects.isNull(savedUserDto)) {
      LOG.error(UserConstants.COULD_NOT_CREATE_USER);
      throw new InvalidServiceRequestException(UserConstants.COULD_NOT_CREATE_USER);
    }
    var encryptedToken = encryptionService.encrypt(verificationToken);
    var encodedToken = encryptionService.encode(encryptedToken);

    emailService.sendAccountVerificationEmail(savedUserDto, encodedToken);

    var location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUserDto.getId())
            .toUri();

    return ResponseEntity.created(location).body(OperationStatus.SUCCESS);
  }

  /**
   * Continuation of sign up is handled by this mapping.
   *
   * @param token the token.
   * @return the view mapping for login.
   */
  @Loggable
  @GetMapping(SignUpConstants.EMAIL_VERIFY_MAPPING)
  public ResponseEntity<OperationStatus> completeSignUp(@RequestParam String token) {

    var decodedToken = encryptionService.decode(token);
    var verificationToken = encryptionService.decrypt(decodedToken);

    var userDto = validateTokenAndUpdateUser(verificationToken);
    // send an account confirmation to the user.
    emailService.sendAccountConfirmationEmail(userDto, customerPortalUrl);

    // automatically authenticate the userDto since there will be a redirection to profile page
    UserDetails userDetails = userService.getUserDetails(userDto.getEmail());
    SecurityUtils.authenticateUser(userDetails);

    var location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userDto.getId())
            .toUri();

    return ResponseEntity.created(location).body(OperationStatus.SUCCESS);
  }

  /**
   * Update the user at this point then send an email after an update if token is valid.
   *
   * @param token the token
   * @return the user dto
   */
  private UserDto validateTokenAndUpdateUser(final String token) {
    if (jwtService.isValidJwtToken(token)) {
      var email = jwtService.getEmailFromToken(token);
      var userDto = userService.findByEmail(email);

      if (Objects.nonNull(userDto)
          && token.equals(userDto.getVerificationToken())
          && isTokenOwnerAndNotEnabled(email, userDto)) {
        return userService.updateUser(userDto, UserHistoryType.VERIFIED);
      }
    }

    LOG.debug(ErrorConstants.INVALID_TOKEN);
    throw new InvalidTokenException(ErrorConstants.INVALID_TOKEN);
  }

  /**
   * Checks if the token owner is the same as the userDto and if the user is not enabled.
   *
   * @param email the email
   * @param userDto the user dto
   * @return if the token owner is the same as the userDto and if the user is not enabled
   */
  private boolean isTokenOwnerAndNotEnabled(String email, UserDto userDto) {
    if (userDto.getEmail().equals(email) && userDto.isEnabled()) {
      LOG.debug(SignUpConstants.ACCOUNT_EXISTS);
      throw new UserAlreadyExistsException(SignUpConstants.ACCOUNT_EXISTS);
    } else if (userDto.getEmail().equals(email) && !userDto.isEnabled()) {
      UserUtils.enableUser(userDto);

      return true;
    }
    return false;
  }
}
