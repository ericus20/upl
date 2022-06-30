const buildEnv = process.env.NODE_ENV || "development";
const envVariables = require("./config")[buildEnv] || {};

// eslint-disable-next-line no-console
console.log(`📦 Using "${buildEnv}" environment variables to build the site.`);

/** @type {import('next').NextConfig} */
const nextConfig = {
  env: {
    ...envVariables,
    buildEnv,
  },
  images: {
    domains: ["links.papareact.com", "fakestoreapi.com"],
  },
  reactStrictMode: true,
};

module.exports = nextConfig;
