import {
  createAsyncThunk,
  createSlice,
  isAnyOf,
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
import axios, { AxiosRequestConfig } from "axios";
import AuthStatus from "enums/AuthStatus";
// eslint-disable-next-line import/no-cycle
import axiosInstance from "libs/axios";
import routes from "routes";
import { alertService } from "services/alert.service";
import Auth from "types/Auth";
import JwtResponse, { initialJwtResponseState } from "types/JwtResponse";
import LoginRequest from "types/LoginRequest";

export interface AuthState {
  principal: JwtResponse;
  isLoggedIn: boolean;
  loading: AuthStatus;
  error?: SerializedError;
}

export const initialAuthState: AuthState = {
  loading: AuthStatus.IDLE,
  error: undefined,
  principal: initialJwtResponseState,
  isLoggedIn: false,
};

export const login = createAsyncThunk(
  "auth/login",
  async (credentials: LoginRequest, thunkAPI) => {
    try {
      const requestOptions: AxiosRequestConfig<string> = {
        baseURL: routes.api.base,
        url: routes.api.login,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
        withCredentials: true,
        data: JSON.stringify(credentials),
      };
      const response = await axios.request<JwtResponse>(requestOptions);

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
      const response = await axiosInstance.get<JwtResponse>(
        routes.api.refreshToken
      );

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
    updateAuth: (
      state: Auth,
      action: PayloadAction<{ principal: JwtResponse }>
    ) => {
      state.principal = action.payload.principal;
    },
    reset: () => initialAuthState,
  },
  extraReducers: builder => {
    builder.addCase(logout.pending, state => {
      state.loading = AuthStatus.LOADING;
    });
    builder.addCase(logout.fulfilled, state => {
      state.loading = AuthStatus.IDLE;
      return initialAuthState;
    });
    builder.addMatcher(isAnyOf(login.pending, refreshToken.pending), state => {
      state.loading = AuthStatus.LOADING;
    });
    builder.addMatcher(
      isAnyOf(login.fulfilled, refreshToken.fulfilled),
      (state: Auth, action: PayloadAction<JwtResponse>) => {
        state.principal = action.payload;
        state.isLoggedIn = !!action.payload.accessToken;
        state.loading = AuthStatus.IDLE;
      }
    );
    builder.addMatcher(
      isAnyOf(login.rejected, refreshToken.rejected),
      (state, action) => {
        Object.assign(state, {
          ...initialAuthState,
          loading: AuthStatus.FAILED,
          error: action.error,
        });

        if (action.error.message) {
          alertService.error("Login Failed", { id: "login-alert" });
        }
      }
    );
  },
});

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectAuth = (state: AppState) => state.authReducer;

export const { updateAuth, reset } = authSlice.actions;

export default authSlice.reducer;
