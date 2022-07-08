import { useAppDispatch, useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import {
  decrementQuantity,
  incrementQuantity,
  updateQuantity,
} from "app/slices/cart";
import Item from "models/item";
import React from "react";

interface QuantityCountProps {
  publicId: string;
  quantity: number;
  dispatch?: boolean;
  setQuantity: React.Dispatch<React.SetStateAction<number>>;
}

const QuantityCount: React.FC<QuantityCountProps> = ({
  publicId: cartItemId,
  quantity,
  dispatch,
  setQuantity,
}) => {
  const quantityDispatch = useAppDispatch();
  const { isLoggedIn } = useAppSelector(selectAuth);

  const updateQuantityHere = async (count: number) => {
    if (dispatch) {
      const cartItem = { publicId: cartItemId, quantity: count };
      quantityDispatch(updateQuantity(cartItem));
    }
  };

  const increaseCount = async () => {
    setQuantity(quantity + 1);
    updateQuantityHere(quantity + 1);

    if (isLoggedIn) {
      await quantityDispatch(incrementQuantity(cartItemId));
    } else {
      const storedItemJson = localStorage.getItem("items");

      if (storedItemJson) {
        const storedItems: Item[] = JSON.parse(storedItemJson);
        const index = storedItems.findIndex(
          cartItem => cartItem.publicId === cartItemId
        );

        if (index >= 0) {
          storedItems[`${index}`].quantity += 1;
        }

        localStorage.setItem("items", JSON.stringify(storedItems));
      }
    }
  };

  const decreaseCount = () => {
    if (quantity > 0) {
      setQuantity(quantity - 1);
      updateQuantityHere(quantity - 1);

      if (isLoggedIn) {
        quantityDispatch(decrementQuantity(cartItemId));
      } else {
        const storedItemJson = localStorage.getItem("items");

        if (storedItemJson) {
          const storedItems: Item[] = JSON.parse(storedItemJson);
          const index = storedItems.findIndex(
            cartItem => cartItem.publicId === cartItemId
          );

          if (index >= 0) {
            storedItems[`${index}`].quantity -= 1;
          }

          localStorage.setItem("items", JSON.stringify(storedItems));
        }
      }
    }
  };

  return (
    <div className="flex items-center">
      <button
        type="button"
        onClick={decreaseCount}
        className="outline-none w-10 h-10 flex items-center bg-[#e8e8e8] justify-center rounded transition-all duration-[.3s] font-bold hover:bg-[#d6d6d6]"
      >
        -
      </button>
      <div className="min-w-[2.1875rem] flex justify-center align-center mx-3 my-0">
        {quantity}
      </div>
      <button
        type="button"
        onClick={increaseCount}
        className="outline-none w-10 h-10 flex items-center bg-[#e8e8e8] justify-center rounded transition-all duration-[.3s] font-bold hover:bg-[#d6d6d6]"
      >
        +
      </button>
    </div>
  );
};

QuantityCount.defaultProps = {
  dispatch: false,
};

export default QuantityCount;
