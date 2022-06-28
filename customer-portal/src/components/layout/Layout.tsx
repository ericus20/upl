import { useAppDispatch } from "app/hooks";
import { refreshToken } from "app/slices/auth";
import { AppDispatch } from "app/store";
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
      <main>{children}</main>
    </div>
  );
};

export default Layout;
