import { useAppDispatch } from "app/hooks";
import { refreshToken } from "app/slices/auth";
import { addCartItemToApi, getUserCartItems, setItems } from "app/slices/cart";
import { AppDispatch } from "app/store";
import Header from "components/core/Header";
import Spinner from "components/core/Spinner";
import RoleType from "enums/RoleType";
import Item from "models/item";
import JwtResponse from "models/response/JwtResponse";
import dynamic from "next/dynamic";
import Head from "next/head";
import { useEffect, useState } from "react";

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
  const [loaded, setLoaded] = useState<boolean>(false);

  useEffect(() => {
    const init = async () => {
      // @ts-expect-error: Let's ignore a compile error
      const { payload }: { payload: JwtResponse } = await dispatch(
        refreshToken()
      );

      if (payload && payload.publicId) {
        const storedItemsJson = localStorage.getItem("items");
        if (storedItemsJson) {
          const items: Item[] = JSON.parse(storedItemsJson);
          items.forEach(item => dispatch(addCartItemToApi(item)));

          localStorage.removeItem("items");
        }

        await dispatch(getUserCartItems(payload.publicId));
      } else {
        const storedItemsJson = localStorage.getItem("items");
        if (storedItemsJson) {
          const items: Item[] = JSON.parse(storedItemsJson);
          dispatch(setItems({ items }));
        }
      }
      setLoaded(true);
    };

    init();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return loaded ? (
    <AuthGuard rolesAllowed={[RoleType.USER, RoleType.ADMIN]}>
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
  ) : (
    <div className="align-middle flex items-center justify-center">
      <Spinner /> loading...
    </div>
  );
};

export default Layout;
