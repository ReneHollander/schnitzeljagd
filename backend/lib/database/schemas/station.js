var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        name: {
            type: String,
            required: true
        },
        description: {
            type: String,
            required: true
        },
        navigation: {
            type: ObjectId,
            ref: 'Navigation',
            required: true
        },
        answer: {
            type: ObjectId,
            ref: 'Answer',
            required: true
        }
    });

    return mongoose.model('Station', schema);
};
