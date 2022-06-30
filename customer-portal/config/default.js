/**
 * Default environment variables for all environments. Individual
 * environment files (e.g production.js) can override these variables,
 * in which case the individual environment's variables are merged
 * with these, in config/index.js.
 *
 * WARNING: No secrets!
 * Only store non-secrets here. Everything in this file can be included in build artifacts.
 * @type {Record<string, string>}
 */
const config = {
  apiUrl: "http://localhost:8080",
};

module.exports = config;
