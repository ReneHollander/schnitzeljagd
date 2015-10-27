var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        stations: [{
            type: ObjectId,
            ref: 'Station'
        }]
    });
    var StationOrder = mongoose.model('StationOrder', schema);
};
