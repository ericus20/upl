import { useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import RoleType from "enums/RoleType";
import Status from "enums/Status";
import Link from "next/link";

interface AuthGuardProps {
  rolesAllowed?: string[];
  customText?: React.ReactNode;
  children: React.ReactNode; // best, accepts everything React can render
}

const AuthGuard: React.FC<AuthGuardProps> = ({
  rolesAllowed,
  customText,
  children,
}) => {
  const { loading, isLoggedIn, principal: user } = useAppSelector(selectAuth);

  if (loading === Status.LOADING) {
    return <>loading...</>;
  }

  // If no role restrictions are provided and user is logged in OR
  // if user is logged in and user has roles specified to be permitted
  if (
    (!rolesAllowed && isLoggedIn) ||
    (isLoggedIn &&
      user &&
      user.roles &&
      user.roles.some(role => rolesAllowed?.includes(role)))
  ) {
    // eslint-disable-next-line react/jsx-no-useless-fragment
    return <>{children}</>;
  }

  return (
    <section className="m-auto mt-20 justify-center items-center">
      <h2 className="text-center">Unauthorized</h2>
      <div className="text-center">
        {customText}

        <p className="text-72 mb-24">
          <Link
            className="link text-primary underline cursor-pointer"
            href="/login"
          >
            Click here
          </Link>{" "}
          to login
        </p>
      </div>
    </section>
  );
};

AuthGuard.defaultProps = {
  rolesAllowed: [RoleType.USER, RoleType.ADMIN],
  customText:
    "You don't have permission to access this page. Please contact an admin.",
};

export default AuthGuard;
