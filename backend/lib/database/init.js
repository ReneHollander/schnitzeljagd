"use strict";

var Promise = require('bluebird');
var path = require('path');
var fs = Promise.promisifyAll(require('fs-extra'));
var cfg = require('../../cfg.js');
var mongoose = Promise.promisifyAll(require('mongoose'));

var connected = false;

module.exports = function () {
    module.exports = function () {
        return Promise.resolve();
    };
    return mongoose.connectAsync(cfg.database.connectionstring);
};

if (false) {
    var schema = require('./schema.js');

    module.exports()
        .then(function () {
            return schema.User.createUser("rene.hollander@hotmail.de", "Rene8888", "1234");
        })
        .then(function (user) {
            return schema.User.verifyToken(user.validationToken);
        })
        .then(function (user) {
            return schema.Team.createTeam('SuperTeam', user);
        }).then(function (team) {
            return schema.User.createUser("p.k@magic-consoles.com", "Toaster1032", "1234")
                .then(function (user2) {
                    return schema.User.verifyToken(user2.validationToken);
                })
                .then(function (user2) {
                    return team.addMember(user2)
                        .then(function (team) {
                            return schema.Team.findTeamForUser(user2);
                        });
                });
        });
}

if (false) {
    var schema = require('./schema.js');

    module.exports()
        .then(function () {
            return schema.User.createUser("rene.hollander@hotmail.de", "Rene8888", "1234");
        })
        .then(function (user) {
            return schema.User.verifyToken(user.validationToken);
        });
}