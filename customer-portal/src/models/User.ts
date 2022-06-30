interface User {
  publicId: string;
  name: string;
  email: string;
  roles: string[];
}

export const initialUser: User = {
  publicId: "",
  name: "",
  email: "",
  roles: [],
};

export default User;
