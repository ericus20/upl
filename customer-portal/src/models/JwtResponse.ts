interface JwtResponse {
  accessToken: string;
  publicId: string;
  username: string;
  type: string;
  email: string;
  roles: string[];
}

export const initialJwtResponseState: JwtResponse = {
  accessToken: "",
  publicId: "",
  username: "",
  type: "",
  email: "",
  roles: [],
};

export default JwtResponse;
