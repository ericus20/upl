interface JwtResponse {
  accessToken: string;
  publicId: string;
  name: string;
  type: string;
  email: string;
  roles: string[];
}

export const initialJwtResponseState: JwtResponse = {
  accessToken: "",
  publicId: "",
  name: "",
  type: "",
  email: "",
  roles: [],
};

export default JwtResponse;
