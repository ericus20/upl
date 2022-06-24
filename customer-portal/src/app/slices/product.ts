import { createSlice, PayloadAction } from "@reduxjs/toolkit";
// eslint-disable-next-line import/no-cycle
import { AppState } from "app/store";
import Product from "types/Product";

export interface ProductState {
  products: Product[];
}

const initialState: ProductState = {
  products: [],
};

export const productSlice = createSlice({
  name: "product",
  initialState,
  reducers: {
    addProduct: (
      state: ProductState,
      action: PayloadAction<{ product: Product }>
    ) => {
      state.products = [...state.products, action.payload.product];
    },
    reset: () => initialState,
  },
});

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectProduct = (state: AppState) => state.productReducer.products;

export const { addProduct, reset } = productSlice.actions;

export default productSlice.reducer;
