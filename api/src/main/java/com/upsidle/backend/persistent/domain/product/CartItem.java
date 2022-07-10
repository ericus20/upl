package com.upsidle.backend.persistent.domain.product;

import com.upsidle.backend.persistent.domain.base.BaseEntity;
import com.upsidle.backend.persistent.domain.user.User;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The cart item model for the application.
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class CartItem extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = -6663829802082693220L;

  private Integer quantity;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
  private User user;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
  private Product product;

  public CartItem(Product product, User user) {
    this.product = product;
    this.user = user;
    this.quantity = 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CartItem cartItem) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getQuantity(), cartItem.getQuantity())
        && Objects.equals(getProduct(), cartItem.getProduct());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof CartItem;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getQuantity(), getProduct());
  }
}
