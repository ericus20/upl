package com.upsidle.backend.persistent.repository;

import com.upsidle.backend.persistent.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Repository
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find user by email.
   *
   * @param email email used to search for user.
   * @return User found.
   */
  User findByEmail(String email);

  /**
   * Find user by email or email.
   *
   * @param username username used to search for user.
   * @param email email used to search for user.
   * @return User found.
   */
  User findByUsernameOrEmail(String username, String email);

  /**
   * Find user by username.
   *
   * @param username username used to search for user.
   * @return User found.
   */
  User findByUsername(String username);

  /**
   * Check if user exists by username.
   *
   * @param username username to check if user exists.
   * @return True if user exists or false otherwise.
   */
  Boolean existsByUsernameOrderById(String username);

  /**
   * Check if user exists by username or email.
   *
   * @param username username to check if user exists.
   * @param email email to check if user exists.
   * @return True if user exists or false otherwise.
   */
  Boolean existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
      String username, String email);

  /**
   * Check if user exists by username and verificationToken.
   *
   * @param username the username
   * @param verificationToken the verification token
   * @return if user exists with the given verification token
   */
  Boolean existsByUsernameAndVerificationTokenOrderById(String username, String verificationToken);

  /**
   * Find user by public id.
   *
   * @param publicId publicId used to search for user.
   * @return User found.
   */
  User findByPublicId(String publicId);

  /**
   * Find all users that failed to verify their email after a certain time.
   *
   * @param allowedDaysToVerify email verification allowed days.
   * @return List of users that failed to verify their email.
   */
  List<User> findByEnabledFalseAndCreatedAtBefore(LocalDateTime allowedDaysToVerify);

  /**
   * Delete the user associated with the given public id.
   *
   * @param publicId public id of the user to delete.
   * @return Number of rows deleted.
   */
  int deleteByPublicId(String publicId);
}
