import RoleType from "enums/RoleType";

interface SignUpRequest {
  name: string;
  email: string;
  password: string;
  passwordConfirm: string;
  roles: RoleType[];
  terms: boolean;
}

export const initialSignUp: SignUpRequest = {
  name: "",
  email: "",
  password: "",
  passwordConfirm: "",
  roles: [RoleType.USER],
  terms: false,
};

export default SignUpRequest;
