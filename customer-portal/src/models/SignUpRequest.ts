import RoleType from "enums/RoleType";

interface SignUpRequest {
  username: string;
  email: string;
  password: string;
  passwordConfirm: string;
  roles: RoleType[];
}

export const initialSignUp: SignUpRequest = {
  username: "",
  email: "",
  password: "",
  passwordConfirm: "",
  roles: [RoleType.USER],
};

export default SignUpRequest;
