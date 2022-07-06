import { useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import Spinner from "components/core/Spinner";
import RoleType from "enums/RoleType";
import Status from "enums/Status";
import Link from "next/link";
import { useRouter } from "next/router";
import routes from "routes";

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
  const router = useRouter();

  if (loading === Status.LOADING) {
    return (
      <>
        <Spinner /> loading...
      </>
    );
  }

  const path = router.asPath.split("?")[0];

  // If the path is publicly allowed OR
  // If no role restrictions are provided and user is logged in OR
  // if user is logged in and user has roles specified to be permitted
  if (
    routes.publicPaths.includes(path) ||
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
            href="/contact"
          >
            Click here
          </Link>{" "}
          to get started
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
