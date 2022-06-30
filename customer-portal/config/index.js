const { merge } = require("lodash");
const defaultEnvVariables = require("./default");

/**
 * Dictionary of environment configs
 * @type {Record<string, Record<string, string>>}
 */
const environments = {
  development: merge({}, defaultEnvVariables, require("./development")),
};

module.exports = environments;
