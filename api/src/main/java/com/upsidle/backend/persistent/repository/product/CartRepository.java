package com.upsidle.backend.persistent.repository.product;

import com.upsidle.backend.persistent.domain.product.Cart;
import com.upsidle.backend.persistent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository for the ShoppingCart.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Repository
@RepositoryRestResource(exported = false)
public interface CartRepository extends JpaRepository<Cart, Long> {

  /**
   * Find shopping cart by user.
   *
   * @param user user used to search for category.
   * @return ShoppingCart found.
   */
  Cart findByUser(User user);
}
