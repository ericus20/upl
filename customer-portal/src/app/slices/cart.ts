/* eslint-disable import/no-cycle */
import {
  createAsyncThunk,
  createSlice,
  PayloadAction,
  SerializedError,
} from "@reduxjs/toolkit";
// eslint-disable-next-line import/no-cycle
import { AppState } from "app/store";
import { AxiosRequestConfig } from "axios";
import Status from "enums/Status";
import axiosInstance from "libs/axios";
import Item from "models/item";
import routes from "routes";

export interface CartState {
  items: Item[];
  loading: Status;
  error?: SerializedError;
}

const initialState: CartState = {
  items: [],
  loading: Status.IDLE,
  error: undefined,
};

export const addCartItemToApi = createAsyncThunk(
  "carts",
  async (item: Item, thunkAPI) => {
    try {
      const requestOptions: AxiosRequestConfig<string> = {
        baseURL: routes.api.base,
        url: routes.api.carts,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
        withCredentials: true,
        data: JSON.stringify(item),
      };
      const response = await axiosInstance.request<string>(requestOptions);

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const removeCartItemFromApi = createAsyncThunk(
  ":publicId",
  async (publicId: string, thunkAPI) => {
    try {
      const requestOptions: AxiosRequestConfig<string> = {
        baseURL: routes.api.base,
        url: `${routes.api.carts}/${publicId}`,
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
        withCredentials: true,
      };
      const response = await axiosInstance.request<string>(requestOptions);

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const getUserCartItems = createAsyncThunk(
  ":publicId/cart",
  async (publicId: string, thunkAPI) => {
    try {
      const response = await axiosInstance.get<Item[]>(
        `${routes.api.users}/${publicId}/cart`
      );

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const incrementQuantity = createAsyncThunk(
  ":publicId/increment",
  async (publicId: string, thunkAPI) => {
    try {
      const response = await axiosInstance.put<string>(
        `${routes.api.carts}/${publicId}/increment`
      );

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const decrementQuantity = createAsyncThunk(
  ":publicId/increment",
  async (publicId: string, thunkAPI) => {
    try {
      const response = await axiosInstance.put<string>(
        `${routes.api.carts}/${publicId}/decrement`
      );

      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue({ error });
    }
  }
);

export const cartSlice = createSlice({
  name: "product",
  initialState,
  // The `reducers` field lets us define reducers and generate associated actions
  reducers: {
    setItems: (state: CartState, action: PayloadAction<{ items: Item[] }>) => {
      // search with the id to find existing item and update the quantity if found
      state.items = action.payload.items;
    },
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
        cartItem => cartItem.publicId === action.payload.publicId
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
        cartItem => cartItem.publicId === action.payload.publicId
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
  extraReducers: builder => {
    builder
      .addCase(getUserCartItems.pending, state => {
        state.loading = Status.LOADING;
      })
      .addCase(getUserCartItems.fulfilled, (state, action) => {
        state.loading = Status.IDLE;
        state.error = undefined;
        state.items = [...action.payload];
      })
      .addCase(getUserCartItems.rejected, (state, action) => {
        Object.assign(state, {
          ...initialState,
          loading: Status.FAILED,
          error: action.error,
        });
      });
  },
});

export const { setItems, addToCart, removeFromCart, updateQuantity } =
  cartSlice.actions;

// The function below is called a selector and allows us to select a value from
// the state. Selectors can also be defined inline where they're used instead of
// in the slice file. For example: `useSelector((state: RootState) => state.counter.value)`
export const selectItems = (state: AppState) => state.cartReducer.items;
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
