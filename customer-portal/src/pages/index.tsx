import Product from "models/Product";
import type { GetServerSideProps, NextPage } from "next";

const Home: NextPage<{ products: Product[] }> = () => {
  return (
    <div className="contianer bg-gray-100">
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
