import { SerializedError } from "@reduxjs/toolkit";
import AuthStatus from "enums/AuthStatus";
import JwtResponse, { initialJwtResponseState } from "./JwtResponse";

interface Auth {
  principal: JwtResponse;
  isLoggedIn: boolean;
  loading: AuthStatus;
  error?: SerializedError;
}

export const initialAuthState: Auth = {
  loading: AuthStatus.IDLE,
  error: undefined,
  principal: initialJwtResponseState,
  isLoggedIn: false,
};

export default Auth;
