import Category from "./product/Category";

interface Product {
  publicId: string;
  title: string;
  price: number;
  description: string;
  image: string;
  active: boolean;
  unitsInStock: number;
  rating: {
    publicId: string;
    rate: number;
    count: number;
  };
  category: Category;
}

export const initialProductState: Product = {
  publicId: "",
  title: "",
  price: 0.0,
  description: "",
  image: "",
  active: false,
  unitsInStock: 1,
  rating: {
    publicId: "",
    rate: 0.0,
    count: 0,
  },
  category: {
    publicId: "",
    name: "",
  },
};

export default Product;
