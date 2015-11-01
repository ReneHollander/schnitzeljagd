var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        name: String,
        description: String,
        navigation: {
            type: ObjectId,
            ref: 'Navigation'
        },
        answer: {
            type: ObjectId,
            ref: 'Answer'
        }
    });

    return mongoose.model('Station', schema);
};
