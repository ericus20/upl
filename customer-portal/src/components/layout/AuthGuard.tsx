import { useAppSelector } from "app/hooks";
import { selectAuth } from "app/slices/auth";
import Login from "components/Login";
import AuthStatus from "enums/AuthStatus";
import RoleType from "enums/RoleType";

interface AuthGuardProps {
  rolesAllowed?: string[];
  message?: string;
  children?: React.ReactNode; // best, accepts everything React can render
}

const AuthGuard: React.FC<AuthGuardProps> = ({
  rolesAllowed,
  message,
  children,
}) => {
  const { loading, principal: user } = useAppSelector(selectAuth);

  if (loading === AuthStatus.LOADING) {
    return <>loading...</>;
  }

  if (
    user &&
    user.roles &&
    user.roles.some(role => rolesAllowed?.includes(role))
  ) {
    // eslint-disable-next-line react/jsx-no-useless-fragment
    return <>{children}</>;
  }

  return message ? (
    <section>
      <h2 className="text-center">Unauthorized</h2>
      <div className="text-center">
        {message ||
          "You don't have permission to access this page. Please contact an admin."}
      </div>
    </section>
  ) : (
    <Login />
  );
};

AuthGuard.defaultProps = {
  rolesAllowed: [RoleType.USER],
  message:
    "You don't have permission to access this page. Please contact an admin.",
  children: null,
};

export default AuthGuard;
