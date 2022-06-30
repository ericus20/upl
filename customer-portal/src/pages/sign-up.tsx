/* eslint-disable jsx-a11y/anchor-is-valid */
/* eslint-disable jsx-a11y/label-has-associated-control */
import { yupResolver } from "@hookform/resolvers/yup";
import { useAppDispatch } from "app/hooks";
import { signUp } from "app/slices/user";
import { AppDispatch } from "app/store";
import Alert from "components/core/Alert";
import Link from "components/core/Link";
import Spinner from "components/core/Spinner";
import AlertId from "enums/AlertId";
import SignUpRequest from "models/request/SignUpRequest";
import { useForm } from "react-hook-form";
import { NextPageWithLayout } from "types/layout";
import * as Yup from "yup";

const SignUp: NextPageWithLayout = () => {
  const dispatch: AppDispatch = useAppDispatch();

  // form validation rules
  const validationSchema = Yup.object().shape({
    name: Yup.string().required("Name is required"),
    email: Yup.string()
      .email("Email must be valid")
      .required("Email is required"),
    password: Yup.string().required("Password is required"),
    passwordConfirm: Yup.string().oneOf(
      [Yup.ref("password"), null],
      "Passwords must match"
    ),
  });
  const formOptions = { resolver: yupResolver(validationSchema) };

  // get functions to build form with useForm() hook
  const { register, handleSubmit, formState } =
    useForm<SignUpRequest>(formOptions);
  const { errors } = formState;

  const onSubmit = async (signUpRequest: SignUpRequest) => {
    await dispatch(signUp(signUpRequest));
  };

  const redOutline = "outline-red-500";
  const greenOutline = "outline-green-500";

  return (
    <div className="flex m-auto mt-14 justify-center items-center">
      <div className="p-4 bg-white rounded-lg border border-gray-200 shadow-md sm:p-4 lg:p-6 dark:bg-gray-800 dark:border-gray-700 w-1/4">
        <Alert id={AlertId.SIGN_UP} />
        <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
          <h5 className="text-lg text-center font-medium text-gray-900 dark:text-white">
            Sign Up
          </h5>
          <div>
            <label
              htmlFor="name"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
            >
              Name
            </label>
            <input
              id="name"
              {...register("name")}
              className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white form-control ${
                errors.name ? redOutline : greenOutline
              }`}
              placeholder="Name"
            />
            <div className="text-red-500 text-xs my-1">
              {errors.name?.message}
            </div>
          </div>
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
              className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white form-control ${
                errors.password ? redOutline : greenOutline
              }`}
            />
            <div className="text-red-500 text-xs my-1">
              {errors.password?.message}
            </div>
          </div>
          <div>
            <label
              htmlFor="passwordConfirm"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300"
            >
              Confirm Password
            </label>
            <input
              type="password"
              {...register("passwordConfirm")}
              id="passwordConfirm"
              placeholder="*****"
              className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white ${
                errors.passwordConfirm ? redOutline : greenOutline
              }`}
            />
            <div className="text-red-500 text-xs my-1">
              {errors.passwordConfirm?.message}
            </div>
          </div>
          <div className="flex items-start">
            <div className="flex items-start">
              <div className="flex items-center h-5">
                <input
                  id="terms"
                  type="checkbox"
                  value=""
                  className="w-4 h-4 bg-gray-50 rounded border border-gray-300 focus:ring-3 focus:ring-blue-300 dark:bg-gray-700 dark:border-gray-600 dark:focus:ring-blue-600 dark:ring-offset-gray-800"
                />
              </div>
              <label
                htmlFor="terms"
                className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
              >
                Terms &amp; condition
              </label>
            </div>
            <Link
              href="/login"
              className="ml-auto text-sm text-blue-700 hover:underline dark:text-blue-500"
            >
              Alredy have an account?
            </Link>
          </div>
          <button
            type="submit"
            disabled={formState.isSubmitting}
            className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          >
            {formState.isSubmitting && <Spinner />}
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
};

SignUp.getLayout = (page: React.ReactElement) => page;

export default SignUp;
