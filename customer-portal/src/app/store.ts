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

const combinedReducers = combineReducers({
  authReducer,
});
export type OurStore = ReturnType<typeof combinedReducers>;

type RootState =
  | CombinedState<{
      authReducer: AuthState;
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
