import {
  createAsyncThunk,
  createSlice,
  SerializedError,
} from "@reduxjs/toolkit";
import axios, { AxiosError, AxiosRequestConfig } from "axios";
import AlertId from "enums/AlertId";
import Status from "enums/Status";
import SignUpRequest from "models/request/SignUpRequest";
import ErrorResponse from "models/response/ErrorResponse";
import User from "models/User";
import routes from "routes";
import { alertService } from "services";

export interface UserState {
  users: User[];
  loading: Status;
  error?: SerializedError;
}

export const initialUserState: UserState = {
  loading: Status.IDLE,
  error: undefined,
  users: [],
};

export const signUp = createAsyncThunk(
  "users",
  async (signUpRequest: SignUpRequest, thunkAPI) => {
    try {
      const requestOptions: AxiosRequestConfig<string> = {
        baseURL: routes.api.base,
        url: routes.api.users,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
        withCredentials: true,
        data: JSON.stringify(signUpRequest),
      };
      const response = await axios.request<string>(requestOptions);

      // Trigger alert for the successful registration pending verification.
      alertService.success(
        "Please check your email to complete registration.",
        { id: AlertId.SIGN_UP }
      );
      return response.data;
    } catch (error) {
      // check if error is an axios error
      if (error && axios.isAxiosError(error)) {
        const err = error as AxiosError<ErrorResponse, unknown>;
        if (err.response) {
          alertService.error(err.response?.data.message, {
            id: AlertId.SIGN_UP,
          });
        }
      }

      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const userSlice = createSlice({
  name: "user",
  initialState: initialUserState,
  reducers: {
    reset: () => initialUserState,
  },
  extraReducers: builder => {
    builder
      .addCase(signUp.pending, state => {
        state.loading = Status.LOADING;
      })
      .addCase(signUp.fulfilled, state => {
        state.loading = Status.IDLE;
        state.error = undefined;
      })
      .addCase(signUp.rejected, (state, action) => {
        Object.assign(state, {
          ...initialUserState,
          loading: Status.FAILED,
          error: action.error,
        });
      });
  },
});
