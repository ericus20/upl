import { updateQuantity } from "app/slices/cart";
import React from "react";
import { useDispatch } from "react-redux";

interface QuantityCountProps {
  publicId: string;
  quantity: number;
  dispatch?: boolean;
  setQuantity: React.Dispatch<React.SetStateAction<number>>;
}

const QuantityCount: React.FC<QuantityCountProps> = ({
  publicId,
  quantity,
  dispatch,
  setQuantity,
}) => {
  const quantityDispatch = useDispatch();

  const updateQuantityHere = (count: number) => {
    if (dispatch) {
      const product = { publicId, quantity: count };
      quantityDispatch(updateQuantity(product));
    }
  };

  const increaseCount = () => {
    setQuantity(quantity + 1);
    updateQuantityHere(quantity + 1);
  };

  const decreaseCount = () => {
    if (quantity > 0) {
      setQuantity(quantity - 1);
      updateQuantityHere(quantity - 1);
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
