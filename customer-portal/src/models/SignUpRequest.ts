import RoleType from "enums/RoleType";

interface SignUpRequest {
  email: string;
  username: string;
  password: string;
  roles: RoleType[];
}

export const initialSignUp: SignUpRequest = {
  email: "",
  username: "",
  password: "",
  roles: [RoleType.USER],
};

export default SignUpRequest;
