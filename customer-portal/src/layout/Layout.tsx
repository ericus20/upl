import { useAppDispatch } from "app/hooks";
import { refreshToken } from "app/slices/auth";
import { AppDispatch } from "app/store";
import Header from "components/core/Header";
import Head from "next/head";
import { useEffect } from "react";

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
  );
};

export default Layout;
