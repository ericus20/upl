package com.upsidle.backend.persistent.domain.product;

import com.upsidle.backend.persistent.domain.base.BaseEntity;
import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
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
public class CartItem extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = -6663829802082693220L;

  private Integer quantity;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
  private Product product;
}
