var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        title: String,
        description: String,
        navigation: {
            type: ObjectId,
            ref: 'Navigation'
        }
    });

    return mongoose.model('Station', schema);
};
