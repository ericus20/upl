import { SerializedError } from "@reduxjs/toolkit";
import AuthStatus from "enums/AuthStatus";
import User, { initialUserState } from "./User";

interface Auth {
  user: User;
  isLoggedIn: boolean;
  loading: AuthStatus;
  error?: SerializedError;
}

export const initialAuthState: Auth = {
  loading: AuthStatus.IDLE,
  error: undefined,
  user: initialUserState,
  isLoggedIn: false,
};

export default Auth;
