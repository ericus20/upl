import { SerializedError } from "@reduxjs/toolkit";
import Status from "enums/Status";
import JwtResponse, { initialJwtResponseState } from "./JwtResponse";

interface Auth {
  principal: JwtResponse;
  isLoggedIn: boolean;
  loading: Status;
  error?: SerializedError;
}

export const initialAuthState: Auth = {
  loading: Status.IDLE,
  error: undefined,
  principal: initialJwtResponseState,
  isLoggedIn: false,
};

export default Auth;
