import {
  Action,
  AnyAction,
  CombinedState,
  combineReducers,
  configureStore,
  ThunkAction,
} from "@reduxjs/toolkit";
import { HYDRATE } from "next-redux-wrapper";
// eslint-disable-next-line import/no-cycle
import authReducer, { AuthState } from "app/slices/auth";
// eslint-disable-next-line import/no-cycle
import productReducer, { ProductState } from "app/slices/product";

const combinedReducers = combineReducers({
  authReducer,
  productReducer,
});
export type OurStore = ReturnType<typeof combinedReducers>;

type RootState =
  | CombinedState<{
      authReducer: AuthState;
      productReducer: ProductState;
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
