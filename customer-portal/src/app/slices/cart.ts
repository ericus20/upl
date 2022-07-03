import { createSlice, PayloadAction } from "@reduxjs/toolkit";
// eslint-disable-next-line import/no-cycle
import { AppState } from "app/store";
import Item from "models/item";

export interface CartState {
  items: Item[];
}

const initialState: CartState = {
  items: [],
};

export const cartSlice = createSlice({
  name: "product",
  initialState,
  // The `reducers` field lets us define reducers and generate associated actions
  reducers: {
    addToCart: (state: CartState, action: PayloadAction<{ item: Item }>) => {
      // search with the id to find existing item and update the quantity if found
      const index = state.items.findIndex(
        cartItem =>
          cartItem.product.publicId === action.payload.item.product.publicId
      );

      if (action.payload.item.quantity > 0) {
        if (index >= 0) {
          state.items[`${index}`].quantity += action.payload.item.quantity;
        } else {
          state.items = [...state.items, action.payload.item];
        }
      }
    },
    removeFromCart: (
      state: CartState,
      action: PayloadAction<{ publicId: string }>
    ) => {
      // searching with id
      const index = state.items.findIndex(
        cartItem => cartItem.product.publicId === action.payload.publicId
      );

      const newBasket = [...state.items];

      if (index >= 0) {
        // The item exists in the basket... remove it...
        newBasket.splice(index, 1);
      }

      state.items = newBasket;
    },
    updateQuantity: (
      state: CartState,
      action: PayloadAction<{ publicId: string; quantity: number }>
    ) => {
      const index = state.items.findIndex(
        cartItem => cartItem.product.publicId === action.payload.publicId
      );

      if (index >= 0) {
        if (action.payload.quantity > 0) {
          state.items[`${index}`].quantity = action.payload.quantity;
        } else {
          const newBasket = [...state.items];
          newBasket.splice(index, 1);

          state.items = newBasket;
        }
      }
    },
    reset: () => initialState,
  },
});

export const { addToCart, removeFromCart, updateQuantity } = cartSlice.actions;

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectItem = (state: AppState) => state.cartReducer.items;
export const selectItemCount = (state: AppState) =>
  state.cartReducer.items.length;

// Computes the total of the items in the cart
export const selectTotal = (state: AppState): number =>
  state.cartReducer.items.reduce(
    (total, item) => total + item.product.price * item.quantity,
    0
  );

// Counts the number of items in the cart
export const selectTotalItems = (state: AppState) =>
  state.cartReducer.items.reduce((total, item) => total + item.quantity, 0);

export default cartSlice.reducer;
