import { useAppDispatch } from "app/hooks";
import { setProductPage } from "app/slices/productPage";
import Banner from "components/core/Banner";
import ProductFeed from "components/product/ProductFeed";
import axiosInstance from "libs/axios";
import ProductPage from "models/ProductPage";
import type { GetServerSideProps, NextPage } from "next";
import { useEffect } from "react";
import routes from "routes";

const Home: NextPage<{ productPage: ProductPage }> = ({ productPage }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    const updateProductPage = async () => {
      await dispatch(setProductPage({ productPage }));
    };

    updateProductPage();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [productPage]);

  return (
    <div className="bg-gray-100">
      <section className="mx-[10.3125rem]">
        {/* Banner */}
        <Banner />

        {/* Product Feed */}
        <ProductFeed />
      </section>
    </div>
  );
};

export const getServerSideProps: GetServerSideProps = async () => {
  const response = await axiosInstance.get<ProductPage>(routes.api.products);
  const productPage = response.data;

  return {
    props: {
      productPage,
    },
  };
};

export default Home;
