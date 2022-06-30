import { useAppDispatch, useAppSelector } from "app/hooks";
import { refreshToken, selectAuth } from "app/slices/auth";
import { AppDispatch } from "app/store";
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
      <main>{children}</main>
    </div>
  );
};

export default Layout;
