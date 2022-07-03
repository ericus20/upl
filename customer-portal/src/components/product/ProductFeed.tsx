import { useAppSelector } from "app/hooks";
import { selectProductPage } from "app/slices/productPage";
import Image from "next/image";
import ProductItem from "./ProductItem";

const ProductFeed = () => {
  const { content: products } = useAppSelector(selectProductPage);

  return (
    <div className="grid grid-flow-row-dense md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 md:-mt-56">
      {products.slice(0, 4).map(product => (
        <ProductItem key={product.publicId} product={product} />
      ))}

      <div className="md:col-span-full relative h-[40vh]">
        <Image
          src="https://links.papareact.com/dyz"
          alt=""
          layout="fill"
          objectFit="cover"
        />
      </div>

      <div className="md:col-span-2">
        {products.slice(4, 5).map(product => (
          <ProductItem key={product.publicId} product={product} />
        ))}
      </div>

      {products.slice(5, products.length).map(product => (
        <ProductItem key={product.publicId} product={product} />
      ))}
    </div>
  );
};

export default ProductFeed;
