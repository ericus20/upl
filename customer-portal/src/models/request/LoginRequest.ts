interface LoginRequest {
  email: string;
  password: string;
}

export const initialLoginRequestState: LoginRequest = {
  email: "",
  password: "",
};

export default LoginRequest;
