var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        teamname: {
            type: String,
            index: true,
            unique: true
        },
        creationDate: Date,
        founder: {type: ObjectId, ref: 'User'},
        members: [{type: ObjectId, ref: 'User'}]
    });
    return mongoose.model('Team', schema);
};
