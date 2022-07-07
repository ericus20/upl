package com.upsidle.backend.persistent.domain.product;

import com.upsidle.backend.persistent.domain.base.BaseEntity;
import com.upsidle.backend.persistent.domain.user.User;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

/**
 * The shopping cart model for the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Cart extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = -625452751753382116L;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
  private User user;

  @JsonIgnore
  @ToString.Exclude
  @OneToMany(mappedBy = "category")
  private Set<Product> products = new HashSet<>();

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<CartItem> cartItems = new HashSet<>();

  public Cart(Product storedProduct, User user) {
    this.products.add(storedProduct);
    this.user = user;
  }

  /**
   * Adds a product to the cart.
   *
   * @param product the product to add to the cart.
   */
  public void addProduct(Product product) {
    products.add(product);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Cart cart) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getUser(), cart.getUser());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Cart;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUser());
  }
}
