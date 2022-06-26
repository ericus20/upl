interface JwtResponse {
  accessToken: string;
  publicId: string;
  username: string;
  email: string;
  roles: string[];
}

export const initialJwtResponseState: JwtResponse = {
  accessToken: "",
  publicId: "",
  username: "",
  email: "",
  roles: [],
};

export default JwtResponse;
