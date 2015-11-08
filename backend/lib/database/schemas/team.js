var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;
var util = require('../../util.js');

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        teamname: {
            type: String,
            index: true,
            unique: true,
            required: true
        },
        creationDate: {
            type: Date,
            default: Date.now
        },
        teampasswordHash: {
            type: String,
            required: true
        },
        founder: {
            type: ObjectId,
            ref: 'User',
            required: true
        },
        members: [{
            type: ObjectId,
            ref: 'User'
        }],
        currentstation: {
            type: ObjectId,
            ref: 'Station'
        },
        completedclusters: [{
            type: ObjectId,
            ref: 'StationCluster'
        }]
    });

    schema.methods.removeMember = function (member) {
        this.members.pull(member);
        return this.save();
    };

    schema.methods.addMember = function (member) {
        this.members.push(member);
        return this.save();
    };

    schema.methods.deleteTeam = function () {
        return this.remove();
    };

    schema.methods.nextStation = function () {
        var that = this;
        return schemas.StationCluster.nextStation(this.currentstation, this.completedclusters)
            .then(function (station) {
                return that.save()
                    .then(function () {
                        return station;
                    })
            });
    };

    schema.statics.createTeam = function (teamname, founder, teampassword) {
        return this.findOne({teamname: teamname})
            .then(function (team) {
                if (team) return Promise.reject('Teamname already in use!');
                else {
                    return bcrypt.hashAsync(teampassword, 8)
                        .then(function (hash) {
                            return new schemas.Team({
                                teamname: teamname,
                                founder: founder,
                                members: founder,
                                teampasswordHash: hash
                            }).save().then(util.promisePopulate(['members', 'founder']));
                        });
                }
            });
    };

    schema.statics.findTeamForUser = function (user) {
        return this.findOne({members: user}).populate(['members', 'founder']);
    };

    return mongoose.model('Team', schema);
};
