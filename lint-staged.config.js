module.exports = {
  // this will check Typescript files in customer portal
  "customer-portal/**/*.{ts,tsx}": () =>
    "yarn --cwd customer-portal tsc --noEmit",

  // this will check Typescript files in admin portal
  "admin-portal/**/*.{ts,tsx}": () => "yarn --cwd admin-portal tsc --noEmit",

  // this will check lint and formatting on all matched files for admin and customer portal
  "*-portal/**/*.{js,json,md,mdx,ts,tsx,yaml,yml}": () => [
    "yarn --cwd customer-portal lint:ci",
    "yarn --cwd customer-portal format-check",
  ],

  // this will check formatting for java and gradle files in api
  "api/**/*.{java,gradle}": () => "yarn format-check-api",
};
