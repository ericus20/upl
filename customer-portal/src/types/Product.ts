interface Product {
  id: number;
  title: string;
  price: number;
  description: string;
  category: string;
  image: string;
  rating: {
    rate: number;
    count: number;
  };
}

export const initialProductState: Product = {
  id: 0,
  title: "",
  price: 0.0,
  description: "",
  category: "",
  image: "",
  rating: {
    rate: 0.0,
    count: 0,
  },
};

export default Product;
