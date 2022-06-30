import { useAppDispatch, useAppSelector } from "app/hooks";
import { refreshToken, selectAuth } from "app/slices/auth";
import { AppDispatch } from "app/store";
import Header from "components/core/Header";
import Head from "next/head";
import { useEffect } from "react";

interface LayoutProps {
  children?: React.ReactNode; // best, accepts everything React can render
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const dispatch: AppDispatch = useAppDispatch();
  const auth = useAppSelector(selectAuth);

  useEffect(() => {
    const requestRefreshtoken = async () => {
      await dispatch(refreshToken());
    };

    if (auth && auth.principal && auth.principal.accessToken) {
      requestRefreshtoken();
    }
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
