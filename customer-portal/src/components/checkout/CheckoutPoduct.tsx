import { removeFromCart } from "app/slices/cart";
import RatingStar from "components/product/RatingStar";
import Item from "models/item";
import Image from "next/image";
import React, { useState } from "react";
import Currency from "react-currency-formatter";
import { useDispatch } from "react-redux";
import QuantityCount from "./QuantityCount";

interface CheckoutProductProps {
  item: Item;
}

const CheckoutProduct: React.FC<CheckoutProductProps> = ({ item }) => {
  const { product, quantity } = item;
  const { publicId, title, price, description, image, rating } = product;

  const dispatch = useDispatch();
  const [quantityUp, setQuantityUp] = useState(quantity);

  const removeItemFromBasket = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    dispatch(removeFromCart({ publicId }));
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
          publicId={publicId}
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
