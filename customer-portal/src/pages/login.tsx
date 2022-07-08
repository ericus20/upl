/* eslint-disable jsx-a11y/anchor-is-valid */
/* eslint-disable jsx-a11y/label-has-associated-control */
import { yupResolver } from "@hookform/resolvers/yup";
import { useAppDispatch } from "app/hooks";
import { login } from "app/slices/auth";
import { AppDispatch } from "app/store";
import Alert from "components/core/Alert";
import Link from "components/core/Link";
import Spinner from "components/core/Spinner";
import AlertId from "enums/AlertId";
import LoginRequest from "models/request/LoginRequest";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { alertService } from "services";
import { NextPageWithLayout } from "types/layout";
import * as Yup from "yup";

/**
 * The Login component.
 *
 * @returns the login page
 */
const Login: NextPageWithLayout = () => {
  const router = useRouter();
  const { query } = useRouter();
  const dispatch: AppDispatch = useAppDispatch();

  useEffect(() => {
    if (query.confirmed) {
      alertService.success("Account successfully created.", {
        id: AlertId.LOGIN,
        autoClose: false,
      });
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [query.confirmed]);

  // form validation rules
  const validationSchema = Yup.object().shape({
    email: Yup.string()
      .email("Email must be valid")
      .required("Email is required"),
    password: Yup.string().required("Password is required"),
  });
  const formOptions = { resolver: yupResolver(validationSchema) };

  // get functions to build form with useForm() hook
  const { register, handleSubmit, formState } =
    useForm<LoginRequest>(formOptions);
  const { errors } = formState;

  /**
   * Function triggered on submitting the login form.
   * Dispatches login action with the specified credentials to the API.
   *
   * @param credentials the login credentials
   */
  const onSubmit = async (credentials: LoginRequest) => {
    const result = await dispatch(login(credentials));

    // Once login is successful, redirect user to home
    if (result.meta.requestStatus === "fulfilled") {
      router.push("/");
    }
  };

  const redOutline = "outline-red-500";
  const greenOutline = "outline-green-500";

  return (
    <div className="flex m-auto mt-14 justify-center items-center">
      <div className="p-4 bg-white rounded-lg border border-gray-200 shadow-md sm:p-6 lg:p-8 dark:bg-gray-800 dark:border-gray-700 w-1/4">
        <Alert id={AlertId.LOGIN} />
        <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
          {/* Heading */}
          <h5 className="text-lg text-center font-medium text-gray-900 dark:text-white">
            Sign in
          </h5>

          {/* Email */}
          <div>
            <label
              htmlFor="email"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
            >
              Email
            </label>
            <input
              id="email"
              type="email"
              {...register("email")}
              className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white form-control ${
                errors.email ? redOutline : greenOutline
              }`}
              placeholder="Email"
            />
            <div className="text-red-500 text-xs my-1">
              {errors.email?.message}
            </div>
          </div>

          {/* Password */}
          <div>
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
            >
              Password
            </label>
            <input
              type="password"
              {...register("password")}
              id="password"
              placeholder="*****"
              className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white ${
                errors.password ? redOutline : greenOutline
              }`}
            />
            <div className="text-red-500 text-xs my-1">
              {errors.password?.message}
            </div>
          </div>

          {/* Remember me and password reset */}
          <div className="flex items-start">
            {/* Remember me */}
            <div className="flex items-start">
              <div className="flex items-center h-5">
                <input
                  id="remember"
                  type="checkbox"
                  value=""
                  className="w-4 h-4 bg-gray-50 rounded border border-gray-300 focus:ring-3 focus:ring-blue-300 dark:bg-gray-700 dark:border-gray-600 dark:focus:ring-blue-600 dark:ring-offset-gray-800"
                />
              </div>
              <label
                htmlFor="remember"
                className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
              >
                Remember me
              </label>
            </div>

            {/* Password reset */}
            <a
              href="#"
              className="ml-auto text-sm text-blue-700 hover:underline dark:text-blue-500"
            >
              Lost Password?
            </a>
          </div>

          {/* Submit button */}
          <button
            type="submit"
            disabled={formState.isSubmitting}
            className={`w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 ${
              formState.isSubmitting &&
              "from-gray-300 to-gray-500 bg-blue-400 border-gray-200 text-gray-500 cursor-not-allowed"
            }`}
          >
            {formState.isSubmitting && <Spinner />}
            {formState.isSubmitting ? "Logging in..." : "Login"}
          </button>

          {/* Create account */}
          <div className="text-sm font-medium text-gray-500 dark:text-gray-300">
            Not registered?{" "}
            <Link
              href="/sign-up"
              className="text-blue-700 hover:underline dark:text-blue-500"
            >
              Create account
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

/**
 * Specify a custom layout to be used in rendering the login page.
 * We do not want to use the default since we don't want the header.
 */
Login.getLayout = (page: React.ReactElement) => page;

export default Login;
