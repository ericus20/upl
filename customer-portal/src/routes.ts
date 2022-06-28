/**
 * @file Routes to various pages in the application are defined here.
 */
const routes = {
  api: {
    base: "http://localhost:8080",
    login: "/api/v1/auth/login",
    logout: "/api/v1/auth/logout",
    users: "/api/v1/users",
    refreshToken: "/api/v1/auth/refresh-token",
  },
};

export default routes;
