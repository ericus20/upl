/* eslint-disable import/no-cycle */
import {
  Action,
  AnyAction,
  CombinedState,
  combineReducers,
  configureStore,
  ThunkAction,
} from "@reduxjs/toolkit";
import authReducer, { AuthState } from "app/slices/auth";
import cartReducer, { CartState } from "app/slices/cart";
import productPageReducer, { ProductPageState } from "app/slices/productPage";
import { HYDRATE } from "next-redux-wrapper";

const combinedReducers = combineReducers({
  authReducer,
  cartReducer,
  productPageReducer,
});

export type OurStore = ReturnType<typeof combinedReducers>;

type RootState =
  | CombinedState<{
      authReducer: AuthState;
      cartReducer: CartState;
      productPageReducer: ProductPageState;
    }>
  | undefined;

const rootReducer = (state: RootState, action: AnyAction) => {
  if (action.type === HYDRATE) {
    return {
      ...state,
      ...action.payload,
    };
  }
  return combinedReducers(state, action);
};

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export type AppState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  AppState,
  unknown,
  Action<string>
>;

export default store;
