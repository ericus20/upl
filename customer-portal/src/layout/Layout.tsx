import { useAppDispatch } from "app/hooks";
import { refreshToken } from "app/slices/auth";
import { AppDispatch } from "app/store";
import Header from "components/core/Header";
import RoleType from "enums/RoleType";
import dynamic from "next/dynamic";
import Head from "next/head";
import { useEffect } from "react";

// Dynamically import the AuthGuard component.
const AuthGuard = dynamic<{
  rolesAllowed?: string[];
  children: React.ReactNode;
}>(() => import("layout/AuthGuard"));

interface LayoutProps {
  children?: React.ReactNode; // best, accepts everything React can render
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const dispatch: AppDispatch = useAppDispatch();

  useEffect(() => {
    const requestRefreshtoken = async () => {
      await dispatch(refreshToken());
    };

    requestRefreshtoken();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <AuthGuard rolesAllowed={[RoleType.USER]}>
      <div>
        <Head>
          <title>Upsidle</title>
          <meta
            name="description"
            content="This is a springboot project for an e-commerce website."
          />
          <link rel="icon" href="/favicon.ico" />
        </Head>

        <Header />
        <main>{children}</main>
      </div>
    </AuthGuard>
  );
};

export default Layout;
