interface User {
  publicId: string;
  username: string;
  email: string;
  roles: string[];
}

export const initialUser: User = {
  publicId: "",
  username: "",
  email: "",
  roles: [],
};

export default User;
