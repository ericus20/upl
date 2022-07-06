import { useAppDispatch } from "app/hooks";
import { setProductPage } from "app/slices/productPage";
import Alert from "components/core/Alert";
import Banner from "components/core/Banner";
import ProductFeed from "components/product/ProductFeed";
import AlertId from "enums/AlertId";
import axiosInstance from "libs/axios";
import ProductPage from "models/ProductPage";
import type { GetServerSideProps, NextPage } from "next";
import { useEffect } from "react";
import routes from "routes";
import { alertService } from "services";

const Home: NextPage<{ productPage: ProductPage }> = ({ productPage }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    const updateProductPage = async () => {
      await dispatch(setProductPage({ productPage }));
    };

    if (Object.keys(productPage).length === 0) {
      alertService.info("No products available", {
        id: AlertId.INDEX,
        autoClose: false,
      });
    }

    updateProductPage();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [productPage]);

  return (
    <div className="bg-gray-100">
      <section className="mx-[10.3125rem]">
        <Alert id={AlertId.INDEX} />
        {/* Banner */}
        <Banner />

        {/* Product Feed */}
        {Object.keys(productPage).length !== 0 && <ProductFeed />}
      </section>
    </div>
  );
};

export const getServerSideProps: GetServerSideProps = async () => {
  const productPage = await axiosInstance
    .get<ProductPage>(routes.api.products)
    .then(response => response.data)
    .catch(() => {
      return {};
    });

  return {
    props: {
      productPage,
    },
  };
};

export default Home;
