// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import Item from "models/item";
import StripeSession from "models/StripeSession";
import type { NextApiRequest, NextApiResponse } from "next";

// eslint-disable-next-line @typescript-eslint/no-var-requires
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY);

const createCheckoutSession = async (
  req: NextApiRequest,
  res: NextApiResponse<StripeSession>
) => {
  const { items, email } = <{ items: Item[]; email: string }>req.body;

  const transformedItems = items.map(item => ({
    description: item.product.description,
    quantity: item.quantity,
    price_data: {
      currency: "USD",
      unit_amount: item.product.price * 100,
      product_data: {
        name: item.product.title,
        images: [item.product.image],
      },
    },
  }));

  const session = await stripe.checkout.sessions.create({
    payment_method_types: ["card"],
    shipping_rates: ["shr_1LI6xEG6SHw3oyQmYKSY1gGW"],
    shipping_address_collection: {
      allowed_countries: ["GB", "US", "CA"],
    },
    line_items: transformedItems,
    mode: "payment",
    success_url: `${process.env.HOST}/success`,
    cancel_url: `${process.env.HOST}/checkout`,
    metadata: {
      email,
      images: JSON.stringify(items.map(item => item.product.image)),
    },
  });

  res.status(200).json({ id: session.id });
};

export default createCheckoutSession;
