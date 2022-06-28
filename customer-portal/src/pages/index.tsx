import Header from "components/core/Header";
import Product from "models/Product";
import type { GetServerSideProps, NextPage } from "next";
import Head from "next/head";

const Home: NextPage<{ products: Product[] }> = () => {
  return (
    <div className="contianer bg-gray-100">
      <Head>
        <title>Upsidle | Home</title>
        <meta
          name="description"
          content="This is a springboot project for an e-commerce website."
        />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />

      <main className="flex text-center justify-center mt-10 text-2xl">
        <h1>Hello World</h1>
      </main>
    </div>
  );
};

export const getServerSideProps: GetServerSideProps = async () => {
  const products = await fetch("https://fakestoreapi.com/products").then(
    response => response.json()
  );

  return {
    props: {
      products,
    },
  };
};

export default Home;
