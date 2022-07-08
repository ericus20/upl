import { useAppDispatch, useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import { removeCartItemFromApi, removeFromCart } from "app/slices/cart";
import RatingStar from "components/product/RatingStar";
import Item from "models/item";
import Image from "next/image";
import React, { useState } from "react";
import Currency from "react-currency-formatter";
import QuantityCount from "./QuantityCount";

interface CheckoutProductProps {
  item: Item;
}

const CheckoutProduct: React.FC<CheckoutProductProps> = ({ item }) => {
  const { publicId: cartItemId, product, quantity } = item;
  const { title, price, description, image, rating } = product;

  const dispatch = useAppDispatch();
  const [quantityUp, setQuantityUp] = useState(quantity);
  const { isLoggedIn } = useAppSelector(selectAuth);

  const removeItemFromBasket = async (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();

    dispatch(removeFromCart({ publicId: cartItemId }));

    if (isLoggedIn) {
      await dispatch(removeCartItemFromApi(cartItemId));
    } else {
      const storedItemJson = localStorage.getItem("items");
      if (storedItemJson) {
        const storedItems: Item[] = JSON.parse(storedItemJson);
        const index = storedItems.findIndex(
          cartItem => cartItem.publicId === cartItemId
        );

        if (index >= 0) {
          // The item exists in the basket... remove it...
          storedItems.splice(index, 1);
        }

        localStorage.setItem("items", JSON.stringify(storedItems));
      }
    }
  };

  return (
    <div className="grid grid-cols-5">
      <Image src={image} height={50} width={50} objectFit="contain" alt="" />

      {/* Middle */}
      <div className="col-span-3 mx-5">
        <p>{title}</p>

        <RatingStar rate={rating.rate} count={rating.count} />

        <p className="text-xs my-2 line-clamp-3">{description}</p>

        <div className="mb-4">
          <Currency quantity={price * quantity} currency="USD" />
        </div>
      </div>

      {/* Right add/remove buttons */}
      <div className="flex flex-col space-y-2 my-auto justify-self-end">
        <QuantityCount
          publicId={cartItemId}
          dispatch
          setQuantity={setQuantityUp}
          quantity={quantityUp}
        />
        <button
          type="button"
          onClick={removeItemFromBasket}
          className="mt-auto button"
        >
          Remove from Busket
        </button>
      </div>
    </div>
  );
};

export default CheckoutProduct;
