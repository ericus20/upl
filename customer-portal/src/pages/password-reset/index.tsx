/* eslint-disable jsx-a11y/anchor-is-valid */
/* eslint-disable jsx-a11y/label-has-associated-control */
import { yupResolver } from "@hookform/resolvers/yup";
import { useAppDispatch } from "app/hooks";
import { passwordReset } from "app/slices/user";
import { AppDispatch } from "app/store";
import Alert from "components/core/Alert";
import Link from "components/core/Link";
import Spinner from "components/core/Spinner";
import AlertId from "enums/AlertId";
import PasswordResetRequest from "models/request/PasswordResetRequest";
import { useForm } from "react-hook-form";
import { alertService } from "services";
import { NextPageWithLayout } from "types/layout";
import * as Yup from "yup";

/**
 * The PasswordReset component.
 *
 * @returns the password reset page
 */
const PasswordReset: NextPageWithLayout = () => {
  const dispatch: AppDispatch = useAppDispatch();

  // form validation rules
  const validationSchema = Yup.object().shape({
    email: Yup.string()
      .email("Email must be valid")
      .required("Email is required"),
  });
  const formOptions = { resolver: yupResolver(validationSchema) };

  // get functions to build form with useForm() hook
  const { register, handleSubmit, formState } =
    useForm<PasswordResetRequest>(formOptions);
  const { errors } = formState;

  /**
   * Function triggered on submitting the password reset form.
   *
   * @param passwordResetRequest the password reset
   */
  const onSubmit = async (passwordResetRequest: PasswordResetRequest) => {
    const result = await dispatch(passwordReset(passwordResetRequest));

    // Once login is successful, redirect user to home
    if (result.meta.requestStatus === "fulfilled") {
      alertService.success(
        "A password reset link has been sent to the email provided.",
        { id: AlertId.PASSWORD_RESET }
      );
    }
  };

  const redOutline = "outline-red-500";
  const greenOutline = "outline-green-500";

  return (
    <div className="flex m-auto mt-16 justify-center items-center">
      <div className="p-4 bg-white rounded-lg border border-gray-200 shadow-md sm:p-6 lg:p-8 dark:bg-gray-800 dark:border-gray-700 w-1/4">
        <Alert id={AlertId.PASSWORD_RESET} />
        <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
          {/* Heading */}
          <div className="flex justify-evenly">
            <h5 className="text-lg text-center font-medium text-gray-900 dark:text-white">
              Reset Password
            </h5>
            <Link className="link" href="/">
              Home
            </Link>
          </div>

          <hr />

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
            {formState.isSubmitting ? "Submitting..." : "Reset Password"}
          </button>

          {/* Create account */}
          <div className="text-sm font-medium text-gray-500 dark:text-gray-300">
            Remembered?{" "}
            <Link
              href="/login"
              className="text-blue-700 hover:underline dark:text-blue-500"
            >
              Login
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
PasswordReset.getLayout = (page: React.ReactElement) => page;

export default PasswordReset;
