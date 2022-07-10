/**
 * @file Routes to various pages in the application are defined here.
 */
const routes = {
  api: {
    base: "http://localhost:8080",
    login: "/api/v1/auth/login",
    logout: "/api/v1/auth/logout",
    users: "/api/v1/users",
    products: "/api/v1/products",
    carts: "/api/v1/carts",
    refreshToken: "/api/v1/auth/refresh-token",
    passwordReset: "/api/v1/password-reset",
  },

  publicPaths: [
    "/login",
    "/sign-up",
    "/terms",
    "/password-reset",
    "/password-reset-complete",
    "/checkout",
    "/",
  ],
};

export default routes;
