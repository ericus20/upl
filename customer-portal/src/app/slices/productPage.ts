import { createSlice, PayloadAction } from "@reduxjs/toolkit";
// eslint-disable-next-line import/no-cycle
import { AppState } from "app/store";
import ProductPage, { initialProductPage } from "models/ProductPage";

export interface ProductPageState {
  productPage: ProductPage;
}

const initialState: ProductPageState = {
  productPage: initialProductPage,
};

export const productPageSlice = createSlice({
  name: "productPage",
  initialState,
  // The `reducers` field lets us define reducers and generate associated actions
  reducers: {
    setProductPage: (
      state: ProductPageState,
      action: PayloadAction<{ productPage: ProductPage }>
    ) => {
      state.productPage = action.payload.productPage;
    },
    reset: () => initialState,
  },
});

export const { setProductPage } = productPageSlice.actions;

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectProductPage = (state: AppState) =>
  state.productPageReducer.productPage;

export default productPageSlice.reducer;
