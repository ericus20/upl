package com.upsidle.backend.persistent.repository.product;

import com.upsidle.backend.persistent.domain.product.CartItem;
import com.upsidle.backend.persistent.domain.product.Product;
import com.upsidle.backend.persistent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  /**
   * Find shopping cart by user.
   *
   * @param user user used to search for cartItem.
   * @param product product used to search for cartItem.
   * @return ShoppingCart found.
   */
  CartItem findByUserAndProduct(User user, Product product);

  /**
   * Find shopping cart by publicId.
   *
   * @param publicId publicId used to search for cartItem.
   * @return ShoppingCart found.
   */
  CartItem findByPublicId(String publicId);

  /**
   * Deletes shopping cart by publicId.
   *
   * @param publicId publicId of cartItem to delete.
   */
  @Modifying
  void deleteByPublicId(String publicId);
}
