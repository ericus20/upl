import {
  createAsyncThunk,
  createSlice,
  PayloadAction,
  SerializedError,
} from "@reduxjs/toolkit";
// Type-only circular references are fine.
// The TS compiler will resolve those at compile time.
// In particular, it's normal to have a slice file export its reducer,
// import the reducer into the store setup, define the RootState type based on that slice,
// and then re-import the RootState type back into a slice file.
// eslint-disable-next-line import/no-cycle
import { AppState } from "app/store";
import AuthStatus from "enums/AuthStatus";
import axiosInstance from "libs/axios";
import routes from "routes";
import Auth from "types/Auth";
import LoginRequest from "types/LoginRequest";
import User, { initialUserState } from "types/User";

export interface AuthState {
  user: User;
  isLoggedIn: boolean;
  loading: AuthStatus;
  error?: SerializedError;
}

export const initialAuthState: AuthState = {
  loading: AuthStatus.IDLE,
  error: undefined,
  user: initialUserState,
  isLoggedIn: false,
};

export const login = createAsyncThunk(
  "auth/login",
  async (credentials: LoginRequest, thunkAPI) => {
    try {
      const response = await axiosInstance.post<User>(
        routes.api.login,
        credentials
      );

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const refreshToken = createAsyncThunk(
  "auth/refreshToken",
  async (_, thunkAPI) => {
    try {
      const response = await axiosInstance.get<User>(routes.api.refreshToken);

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const logout = createAsyncThunk("auth/logout", async (_, thunkAPI) => {
  try {
    const response = await axiosInstance.delete<{ accessToken: string }>(
      routes.api.logout
    );

    return response.data;
  } catch (error) {
    return thunkAPI.rejectWithValue({ error });
  }
});

export const authSlice = createSlice({
  name: "auth",
  initialState: initialAuthState,
  reducers: {
    updateAuth: (state: Auth, action: PayloadAction<{ user: User }>) => {
      state.user = action.payload.user;
    },
    reset: () => initialAuthState,
  },
});

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectAuth = (state: AppState) => state.authReducer;

export const { updateAuth, reset } = authSlice.actions;

export default authSlice.reducer;
