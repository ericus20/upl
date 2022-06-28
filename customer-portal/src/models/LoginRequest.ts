interface LoginRequest {
  username: string;
  password: string;
}

export const initialLoginRequestState: LoginRequest = {
  username: "",
  password: "",
};

export default LoginRequest;
