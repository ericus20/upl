import { loadStripe } from "@stripe/stripe-js";
import { AuthState, selectAuth } from "app/slices/auth";
import { selectItems, selectTotal } from "app/slices/cart";
import axios from "axios";
import CheckoutProduct from "components/checkout/CheckoutPoduct";
import Alert from "components/core/Alert";
import AlertId from "enums/AlertId";
import { Set } from "immutable";
import Category from "models/product/Category";
import StripeSession from "models/StripeSession";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import Currency from "react-currency-formatter";
import { useSelector } from "react-redux";
import { alertService } from "services";

const stripePromise = loadStripe(process.env.STRIPE_PUBLIC_KEY);

const Checkout = () => {
  const { isLoggedIn, principal }: AuthState = useSelector(selectAuth);
  const items = useSelector(selectItems);
  const total = useSelector(selectTotal);
  const [categories, setCategories] = useState<Category[]>([]);

  const createCheckoutSession = async (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();

    const stripe = await stripePromise;

    // Call the backend to create a checkout session...
    const checkoutSession = await axios.post<StripeSession>(
      "/api/create-checkout-session",
      {
        items,
        email: principal.email,
      }
    );

    // Redirect user/customer to Stripe Checkout
    const result = await stripe?.redirectToCheckout({
      sessionId: checkoutSession.data.id,
    });

    if (result?.error) {
      const message = result.error.message
        ? result.error.message
        : "Error redirecting to checkout";

      alertService.error(message, {
        id: AlertId.CHECKOUT,
      });
    }
  };

  useEffect(() => {
    const allCategories = items.map(item => item.product.category);
    const unique = [...Set<Category>(allCategories)];

    setCategories(unique);
  }, [items]);

  return (
    <div className="bg-gray-100">
      <Alert id={AlertId.CHECKOUT} />
      <section className="lg:flex max-w-screen-2xl mx-auto">
        {/* Left */}
        <div className="flex-grow m-5 shadow-sm">
          <Image
            alt=""
            src="https://image.shutterstock.com/image-photo/customer-stand-near-bar-counter-600w-1751405471.jpg"
            width={1020}
            height={250}
            objectFit="fill"
          />

          <div className="flex flex-col p-5 space-y-10 bg-white">
            <h1 className="text-3xl border-b pb-4">
              {items.length === 0 ? "Your cart is empty." : "Shopping Basket"}
            </h1>

            <div className="mb-5">
              {!!categories.length &&
                categories.map(category => (
                  <React.Fragment key={category.publicId}>
                    <h1 className="text-xl pb-4 font-medium capitalize">
                      {category.name}
                    </h1>

                    <hr className="my-2" />
                    <div className="mb-14 space-y-5">
                      {!!items.length &&
                        items
                          .filter(
                            item => item.product.category.name === category.name
                          )
                          .map(item => (
                            <CheckoutProduct
                              key={item.product.publicId}
                              item={item}
                            />
                          ))}
                    </div>
                  </React.Fragment>
                ))}
            </div>
          </div>
        </div>

        {/* Right */}
        <div className="flex flex-col bg-white p-10 shadow-md mr-5 my-5">
          {items.length > 0 && (
            <>
              <h2 className="whitespace-nowrap">
                Subtotal ({items.length} items):{" "}
                <span className="font-bold">
                  <Currency quantity={total} currency="USD" />
                </span>
              </h2>

              <button
                role="link"
                type="button"
                onClick={createCheckoutSession}
                disabled={!isLoggedIn}
                className={`button mt-2 ${
                  !isLoggedIn &&
                  "from-gray-300 to-gray-500 border-gray-200 text-gray-500 cursor-not-allowed"
                }`}
              >
                {!isLoggedIn ? "Sign on to checkout" : "Proceed to checkout"}
              </button>
            </>
          )}
        </div>
      </section>
    </div>
  );
};

export default Checkout;
