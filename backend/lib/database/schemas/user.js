var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var randomstring = require("randomstring");
var gravatar = require('gravatar');

module.exports = function (schemas) {
    var schema = mongoose.Schema({
        email: {
            type: String,
            index: true,
            unique: true
        },
        username: {
            type: String,
            index: true,
            unique: true
        },
        passwordHash: String,
        registrationDate: {
            type: Date,
            default: Date.now
        },
        validationToken: {
            type: String,
            default: function () {
                return randomstring.generate(32);
            },
            index: true
        },
        role: {
            type: String,
            default: 'user'
        }
    });

    schema.virtual('profilepicture').get(function () {
        return gravatar.url(this.email, {s: 512, r: 'pg', d: '404'});
    });

    schema.methods.getTeam = function () {
        return schemas.Team.findTeamForUser(this);
    };

    schema.statics.createUser = function (email, username, password) {
        return this.findOne({$or: [{email: email}, {username: username}]})
            .then(function (user) {
                if (user) {
                    if (user.email === email) return Promise.reject('E-Mail already in use!');
                    if (user.username === username) return Promise.reject('Username already in use!');
                } else {
                    return bcrypt.hashAsync(password, 8)
                        .then(function (hash) {
                            return new schemas.User({
                                email: email,
                                username: username,
                                passwordHash: hash
                            }).save();
                        });
                }
            });
    };

    schema.statics.getUserById = function (id) {
        return this.findOne({_id: id})
            .then(function (user) {
                if (!user) return Promise.reject("User not found!");
                else return user;
            });
    };

    schema.statics.verifyToken = function (token) {
        return this.findOne({validationToken: token})
            .then(function (user) {
                if (user) {
                    user.validationToken = "";
                    return user.save();
                } else {
                    return Promise.reject("Invalid Validation Token")
                }
            });
    };

    schema.statics.validateUser = function (email, password) {
        return this.findOne({email: email})
            .then(function (user) {
                if (user) {
                    if (user.validationToken) {
                        return Promise.reject('E-Mail address not yet validated');
                    } else {
                        return bcrypt.compareAsync(password, user.passwordHash)
                            .then(function (res) {
                                if (res) return user;
                                else return Promise.reject('Invalid Username or Password');
                            });
                    }
                } else return Promise.reject('Invalid Username or Password');
            });
    };

    return mongoose.model('User', schema);
};
