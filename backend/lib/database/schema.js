var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));

function requireSchema(name) {
    return require('./schemas/' + name + '.js')(module.exports);
}

module.exports.User = requireSchema('user');
module.exports.Team = requireSchema('team');