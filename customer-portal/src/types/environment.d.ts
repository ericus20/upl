declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NODE_ENV: "development" | "production" | "test";
      STRIPE_PUBLIC_KEY: string;
      STRIPE_SECRET_KEY: string;
    }
  }
}
