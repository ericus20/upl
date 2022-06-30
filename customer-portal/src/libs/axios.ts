/* eslint-disable import/no-cycle */
import { updateAuth } from "app/slices/auth";
import store, { AppDispatch } from "app/store";
import axios from "axios";
import createAuthRefreshInterceptor from "axios-auth-refresh";
import routes from "routes";

// Create axios instance.
const axiosInstance = axios.create({
  baseURL: process.env.apiUrl,
  withCredentials: true,
});

// Create axios interceptor
createAuthRefreshInterceptor(axiosInstance, failedRequest =>
  // 1. First try request fails - refresh the token.
  axiosInstance
    .get(routes.api.refreshToken)
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
      const { accessToken, type } = resp.data;
      // 2. Set up new access token (e.g. of type is Bearer producing "Bearer accessTokenValue")
      const bearer = `${type} ${accessToken}`;
      axiosInstance.defaults.headers.common.Authorization = bearer;

      // 3. Set up access token of the failed request.
      const failedRequestObject = { ...failedRequest };
      failedRequestObject.response.config.headers.Authorization = bearer;

      // 4. Retry the request with new setup!
      return Promise.resolve();
    })
);

export default axiosInstance;
