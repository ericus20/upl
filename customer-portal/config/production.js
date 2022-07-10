/**
 * Local development environment's public environment variables.
 * WARNING: No secrets!
 * Only store non-secrets here. Everything in this file can be included in build artifacts.
 * @type {Record<string, string>}
 */
const config = {
  // Not finding what you're looking for? Check default.js
  envName: "development",
  domain: "localhost",
};

module.exports = config;
