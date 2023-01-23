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

/**
 * The Home page is the landing page where all products are rendered. temp
 *
 * @param productPage a paged response of the products returned by the SSR
 *
 * @returns the home page
 */
const Home: NextPage<{ productPage: ProductPage }> = ({ productPage }) => {
  const dispatch = useAppDispatch();

  /**
   * Updates the state of the products with the newly received products.
   *
   * If no products were received, sends an alert with appropriate message.
   */
  useEffect(() => {
    const updateProductPage = () => {
      dispatch(setProductPage({ productPage }));
    };

    updateProductPage();

    if (Object.keys(productPage).length === 0) {
      alertService.info("No products available", {
        id: AlertId.INDEX,
        autoClose: false,
      });
    }

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

/**
 * Retrieve the products from the API and pass it over to the component.
 *
 * @returns the productPage
 */
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
