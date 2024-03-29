package com.upsidle.web.rest.v1;

import com.upsidle.annotation.Loggable;
import com.upsidle.backend.service.security.CookieService;
import com.upsidle.backend.service.security.EncryptionService;
import com.upsidle.backend.service.security.JwtService;
import com.upsidle.constant.ErrorConstants;
import com.upsidle.constant.SecurityConstants;
import com.upsidle.enums.OperationStatus;
import com.upsidle.enums.TokenType;
import com.upsidle.shared.util.core.SecurityUtils;
import com.upsidle.web.payload.request.LoginRequest;
import com.upsidle.web.payload.response.JwtResponseBuilder;
import com.upsidle.web.payload.response.LogoutResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.time.Duration;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class attempt to authenticate with AuthenticationManager bean, add authentication object to
 * SecurityContextHolder then Generate JWT token, then return JWT to client.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConstants.API_V1_AUTH_ROOT_URL)
public class AuthController {

  private static final int NUMBER_OF_MINUTES_TO_EXPIRE = 30;

  private final JwtService jwtService;
  private final CookieService cookieService;
  private final EncryptionService encryptionService;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;

  /**
   * Attempts to authenticate with the provided credentials. If successful, a JWT token is returned
   * with some user details.
   *
   * <p>A refresh token is generated and returned as a cookie.
   *
   * @param refreshToken The refresh token
   * @param loginRequest the login request
   * @return the jwt token details
   */
  @Loggable
  @SecurityRequirements
  @PostMapping(value = SecurityConstants.LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponseBuilder> authenticateUser(
      @CookieValue(required = false) String refreshToken,
      @Valid @RequestBody LoginRequest loginRequest) {

    var email = loginRequest.getEmail();
    // Authentication will fail if the credentials are invalid and throw exception.
    SecurityUtils.authenticateUser(authenticationManager, email, loginRequest.getPassword());

    var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    var isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    var responseHeaders = new HttpHeaders();
    // If the refresh token is valid, then we will not generate a new refresh token.
    String newAccessToken = updateCookies(email, isRefreshTokenValid, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(newAccessToken);

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(JwtResponseBuilder.buildJwtResponse(encryptedAccessToken));
  }

  /**
   * Refreshes the current access token and refresh token accordingly.
   *
   * @param refreshToken The refresh token
   * @param request The request
   * @return the jwt token details
   */
  @Loggable
  @SecurityRequirements
  @GetMapping(value = SecurityConstants.REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponseBuilder> refreshToken(
      @CookieValue String refreshToken, HttpServletRequest request) {

    var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
    boolean isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

    if (!isRefreshTokenValid) {
      throw new IllegalArgumentException(ErrorConstants.INVALID_TOKEN);
    }
    var email = jwtService.getEmailFromToken(decryptedRefreshToken);
    var userDetails = userDetailsService.loadUserByUsername(email);

    SecurityUtils.validateUserDetailsStatus(userDetails);
    SecurityUtils.authenticateUser(request, userDetails);

    var responseHeaders = new HttpHeaders();
    String newAccessToken = updateCookies(email, false, responseHeaders);
    String encryptedAccessToken = encryptionService.encrypt(newAccessToken);

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(JwtResponseBuilder.buildJwtResponse(encryptedAccessToken));
  }

  /**
   * Logout the user from the system and clear all cookies from request and response.
   *
   * @param request the request
   * @param response the response
   * @return response entity
   */
  @Loggable
  @SecurityRequirements
  @DeleteMapping(value = SecurityConstants.LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LogoutResponse> logout(
      HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.logout(request, response);

    var responseHeaders = cookieService.addDeletedCookieToHeaders(TokenType.REFRESH);
    var logoutResponse = new LogoutResponse(OperationStatus.SUCCESS);
    SecurityUtils.clearAuthentication();

    return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
  }

  /**
   * Creates a refresh token if expired and adds it to the cookies.
   *
   * @param email the email
   * @param isRefreshValid if the refresh token is valid
   * @param headers the http headers
   */
  private String updateCookies(String email, boolean isRefreshValid, HttpHeaders headers) {

    if (!isRefreshValid) {
      var token = jwtService.generateJwtToken(email);
      var refreshDuration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

      var encryptedToken = encryptionService.encrypt(token);
      cookieService.addCookieToHeaders(headers, TokenType.REFRESH, encryptedToken, refreshDuration);
    }

    var accessTokenExpiration = DateUtils.addMinutes(new Date(), NUMBER_OF_MINUTES_TO_EXPIRE);
    return jwtService.generateJwtToken(email, accessTokenExpiration);
  }
}
