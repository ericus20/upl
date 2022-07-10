import {
  HeartIcon,
  MenuIcon,
  SearchIcon,
  ShoppingCartIcon,
  UserCircleIcon,
} from "@heroicons/react/outline";
import { useAppDispatch, useAppSelector } from "app/hooks";
import { logout, selectAuth } from "app/slices/auth";
import { selectItemCount } from "app/slices/cart";
import { selectProductPage } from "app/slices/productPage";
import Autocomplete from "components/Autocomplete";
import Product from "models/Product";
import React, { useState } from "react";
import { useSelector } from "react-redux";
import { alertService } from "services";
import Link from "./Link";

const Header = () => {
  const { content: products } = useSelector(selectProductPage);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Product[]>([]);
  const [showResults, setShowResults] = useState<boolean>(false);
  const numberOfProductsInCart = useSelector(selectItemCount);
  const { isLoggedIn, principal: user } = useAppSelector(selectAuth);
  const dispatch = useAppDispatch();

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();

    let term = e.target.value;
    term = term.toLowerCase();
    setSearchTerm(term);

    setSearchResults(
      products?.filter(product => product.title.toLowerCase().includes(term))
    );
  };

  const signOut = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();

    const result = await dispatch(logout());

    if (result.meta.requestStatus === "fulfilled") {
      alertService.success("Logout Successful", {
        fade: true,
      });
    }
  };

  return (
    <header className="sticky top-0 z-50">
      {/* Top Nav */}
      <div className="flex mx-10 items-center bg-upsidle_white p-1 flex-grow py-2">
        <div className="m-2 flex items-center cursor-pointer uppercase mx-5 font-bold text-2xl flex-grow sm:flex-grow-0">
          {/* <Image
              onClick={() => router.push("/")}
              src="https://links.papareact.com/f90"
              width={150}
              height={40}
              alt="Amazon Logo"
              objectFit="contain"
              className="cursor-pointer"
            /> */}
          <Link href="/">upsidle</Link>
        </div>

        {/* Search */}
        <div className="hidden mx-20 relative items-center flex-grow cursor-pointer rounded-md h-10 bg-yellow-400 sm:flex hover:bg-yellow-500">
          <select
            className="h-full text-center rounded-l-md bg-[#d4d4d4] focus:outline-yellow-400 p-2 w-auto border-1"
            name="cateogry"
            id="category"
          >
            <option value="all">category</option>
          </select>
          <input
            type="text"
            value={searchTerm}
            placeholder="Search products..."
            onChange={handleSearch}
            onBlur={() => setShowResults(false)}
            onFocus={() => setShowResults(true)}
            onClick={() => setShowResults(true)}
            className="p-2 h-full border-1 flex-grow flex-shrink focus:outline-yellow-400 px-4"
          />
          <SearchIcon className="h-12 p-4" />

          {showResults && (
            <Autocomplete
              searchTerm={searchTerm}
              searchResults={searchResults}
              setShowResults={setShowResults}
            />
          )}
        </div>

        {/* Right Side */}
        <div className="flex items-center text-xs space-x-10 mx-6 whitespace-nowrap">
          <Link
            href="/checkout"
            className="relative link flex items-center space-x-2"
          >
            <ShoppingCartIcon className="h-10" />
            <span className="absolute top-0 right-0 md:right-10 h-4 w-4 bg-yellow-400 text-center rounded-full text-black font-bold">
              {numberOfProductsInCart}
            </span>
            <p className="hidden md:inline md:text-sm mt-2">Cart</p>
          </Link>

          <div className="link flex items-center space-x-2">
            <HeartIcon className="h-6" />
            <p className="md:text-sm">Wish List</p>
          </div>

          {!isLoggedIn && (
            <Link href="/login">
              <div className="link flex items-center space-x-2">
                <UserCircleIcon className="h-6" />
                <p className="md:text-sm">Sign In</p>
              </div>
            </Link>
          )}

          {isLoggedIn && (
            <div
              role="button"
              aria-hidden="true"
              onClick={signOut}
              className="link flex items-center space-x-2"
            >
              <UserCircleIcon className="h-6" />
              <span>Welcome {user.name.split(" ")[0]}</span>
              <p className="md:text-sm">Sign Out</p>
            </div>
          )}
        </div>
      </div>

      {/* Bottom Nav */}
      <div className="flex items-center space-x-3 p-2 pl-6 bg-upsidle_white-light text-sm">
        <div className="link flex items-center">
          <MenuIcon className="h-6 mr-2" />
          All
        </div>
        <p className="link">Books</p>
        <p className="link">Business</p>
        <p className="link">Today&apos;s Deals</p>
        <p className="link hidden lg:inline-flex">Electronics</p>
        <p className="link hidden lg:inline-flex">Food &amp; Grocery</p>
        <p className="link hidden lg:inline-flex">Buy Again</p>
        <p className="link hidden lg:inline-flex">Shopper Toolkit</p>
        <p className="link hidden lg:inline-flex">Health &amp; Personal Care</p>
        <p className="link hidden lg:inline-flex">Electronics</p>
      </div>
    </header>
  );
};

export default Header;
