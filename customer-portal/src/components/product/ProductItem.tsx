import { useAppDispatch, useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import { addCartItemToApi, addToCart } from "app/slices/cart";
import { AppDispatch } from "app/store";
import Item from "models/item";
import Product from "models/Product";
import Image from "next/image";
import React, { useState } from "react";
import Currency from "react-currency-formatter";
import { v4 as uuid } from "uuid";
import RatingStar from "./RatingStar";

interface ProductItemProps {
  product: Product;
}

const ProductItem: React.FC<ProductItemProps> = ({ product }) => {
  const { title, price, description, category, image, rating } = product;

  const dispatch: AppDispatch = useAppDispatch();
  const [rate] = useState<number>(rating.rate);
  const [count] = useState<number>(rating.count);
  const [added, setAdded] = useState<boolean>(false);
  const { isLoggedIn } = useAppSelector(selectAuth);

  const addItemToCart = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const item: Item = { publicId: uuid(), product, quantity: 1 };

    await dispatch(addToCart({ item }));

    if (isLoggedIn) {
      await dispatch(addCartItemToApi(item));
    } else {
      // save item to local storage
      const storedItemJson = localStorage.getItem("items");

      if (storedItemJson) {
        const storedItems: Item[] = JSON.parse(storedItemJson);
        const index = storedItems.findIndex(
          cartItem => cartItem.product.publicId === item.product.publicId
        );

        if (index >= 0) {
          storedItems[`${index}`].quantity += 1;
        } else {
          storedItems.push(item);
        }

        localStorage.setItem("items", JSON.stringify(storedItems));
      } else {
        localStorage.setItem("items", JSON.stringify([item]));
      }
    }

    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  return (
    <div className="relative flex flex-col m-5 bg-white z-30 p-5 transition-transform duration-300 transform origin-bottom scale-100 hover:scale-105">
      <p className="absolute top-2 right-2 text-xs italic text-gray-400">
        {category.name}
      </p>

      <Image
        src={image}
        height={100}
        width={100}
        objectFit="contain"
        alt={title}
      />

      <h4 className="my-3">{title}</h4>

      <RatingStar rate={rate} count={count} />

      <p className="text-xs my-2 line-clamp-2">{description}</p>

      <div className="mb-5">
        <Currency quantity={price} currency="USD" />
      </div>

      <button type="button" onClick={addItemToCart} className="mt-auto button">
        {added ? "Added" : "Add to Cart"}
      </button>
    </div>
  );
};

export default ProductItem;
