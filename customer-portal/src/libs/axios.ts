/* eslint-disable import/no-cycle */
import { updateAuth } from "app/slices/auth";
import store, { AppDispatch } from "app/store";
import axios from "axios";
import createAuthRefreshInterceptor from "axios-auth-refresh";
import cookie from "cookie";
import setCookie from "set-cookie-parser";

// Create axios instance.
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

// Create axios interceptor
createAuthRefreshInterceptor(axiosInstance, failedRequest =>
  // 1. First try request fails - refresh the token.
  axiosInstance
    .get("/api/refreshToken")
    .then(resp => {
      const { dispatch }: { dispatch: AppDispatch } = store;
      const principal = resp.data;
      dispatch(updateAuth({ principal }));

      return resp;
    })
    .then(resp => {
      // 1a. Clear old helper cookie used in 'authorize.ts' higher order function.
      if (axiosInstance.defaults.headers.common.setCookie) {
        delete axiosInstance.defaults.headers.common.setCookie;
      }
      const { accessToken } = resp.data;
      // 2. Set up new access token
      const bearer = `Bearer ${accessToken}`;
      axiosInstance.defaults.headers.common.Authorization = bearer;

      // 3. Set up new refresh token as cookie
      const setCookieResponseHeader = resp.headers["set-cookie"];
      const responseCookieString: string = setCookieResponseHeader
        ? setCookieResponseHeader.toString()
        : "";
      const responseCookie = setCookie.parse(responseCookieString)[0]; // 3a. We can't just acces it, we need to parse it first.
      axiosInstance.defaults.headers.common.setCookie = responseCookieString; // 3b. Set helper cookie for 'authorize.ts' Higher order Function.
      axiosInstance.defaults.headers.common.cookie = cookie.serialize(
        responseCookie.name,
        responseCookie.value
      );

      // 4. Set up access token of the failed request.
      const failedRequestObject = { ...failedRequest };
      failedRequestObject.response.config.headers.Authorization = bearer;

      // 5. Retry the request with new setup!
      return Promise.resolve();
    })
);

export default axiosInstance;
