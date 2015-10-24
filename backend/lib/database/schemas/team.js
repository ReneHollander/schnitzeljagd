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
        creationDate: {
            type: Date,
            default: Date.now
        },
        founder: {type: ObjectId, ref: 'User'},
        members: [{type: ObjectId, ref: 'User'}]
    });

    schema.statics.createTeam = function (teamname, founder) {
        return new schemas.Team({
            teamname: teamname,
            founder: founder._id
        }).save();
    };

    return mongoose.model('Team', schema);
};
