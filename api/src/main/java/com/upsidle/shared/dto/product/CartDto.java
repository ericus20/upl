package com.upsidle.shared.dto.product;

import com.upsidle.shared.dto.BaseDto;
import com.upsidle.shared.dto.UserDto;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The UserDto transfers cart details from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CartDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -6342630857637389028L;

  private UserDto user;

  private Set<ProductDto> products = new HashSet<>();
}
