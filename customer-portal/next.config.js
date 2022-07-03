const buildEnv = process.env.NODE_ENV || "development";
const envVariables = require("./config")[buildEnv] || {};

// eslint-disable-next-line no-console
console.log(`📦 Building with "${buildEnv}" environment variables.`);

/** @type {import('next').NextConfig} */
const nextConfig = {
  env: {
    ...envVariables,
    buildEnv,
    stripePublicKey: process.env.STRIPE_PUBLIC_KEY,
  },
  images: {
    domains: [
      "links.papareact.com",
      "fakestoreapi.com",
      "image.shutterstock.com",
    ],
  },
  reactStrictMode: true,
};

module.exports = nextConfig;
