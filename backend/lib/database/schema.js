var Promise = require('bluebird');
var extend = require('extend');
var mongoose = Promise.promisifyAll(require('mongoose'));

function requireSchema(name) {
    return require('./schemas/' + name + '.js')(module.exports);
}

module.exports.User = requireSchema('user');
module.exports.Team = requireSchema('team');
module.exports.Navigation = requireSchema('navigation');
module.exports.Answer = requireSchema('answer');
module.exports.Station = requireSchema('station');
