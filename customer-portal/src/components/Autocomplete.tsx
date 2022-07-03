import Product from "models/Product";
import Link from "next/link";
import React from "react";
import Currency from "react-currency-formatter";

interface AutocompleteProps {
  searchTerm: string;
  searchResults: Product[];
  setShowResults: React.Dispatch<React.SetStateAction<boolean>>;
}

const Autocomplete: React.FC<AutocompleteProps> = ({
  searchTerm,
  searchResults,
  setShowResults,
}) => {
  return (
    <div
      role="button"
      aria-hidden="true"
      onClick={() => setShowResults(true)}
      onMouseOver={() => setShowResults(true)}
      onFocus={() => setShowResults(true)}
      onMouseLeave={() => setShowResults(false)}
      className="absolute w-full bg-white bottom-0 z-10 rounded-md overflow-y-auto max-h-80 h-auto translate-y-full min-h-[10rem]"
    >
      {searchResults.length ? (
        searchResults.map(({ publicId, title, price, category }) => (
          <div
            key={publicId}
            className="p-2 mt-2 border-b-2 hover:bg-gray-100 rounded-md border-gray-100 bg-gray-50"
          >
            <Link href="/">
              <h5 className="font-medium text-sm text-gray-600">{title}</h5>
            </Link>
            <Link href="/">
              <p className="flex flex-col text-xs text-gray-400">
                <span>{category.name}</span>
                <Currency quantity={price} />
              </p>
            </Link>
          </div>
        ))
      ) : (
        <p>
          {searchTerm && (
            <span className="text-xs text-gray-400 text-center py-2">
              No product found
            </span>
          )}
        </p>
      )}
    </div>
  );
};

export default Autocomplete;
