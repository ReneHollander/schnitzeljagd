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

var schema = require('./schema.js');

if (false) {
    module.exports()
        .then(function () {
            return schema.Navigation.Text({content: "This is the navigation of Station 1"}).save();
        })
        .then(function (navigation) {
            return schema.Answer.QR({title: "Find the QR Code to solve Station 1"}).save()
                .then(function (answer) {
                    return schema.Station({
                        name: "Station 1",
                        description: "This is Station 1",
                        answer: answer,
                        navigation: navigation
                    }).save();
                });
        })
        .then(function (station) {
            return schema.StationCluster.createCluster("Station 1 und 2", "In diesem Cluster sind Station 1 und 2 enthalten", [station]);
        })
        .then(function (cluster) {
            return module.exports()
                .then(function () {
                    return schema.Navigation.Compass({
                        text: 'Find your way to the Stift Klosterneuburg',
                        target: {
                            lat: 48.307311,
                            lang: 16.325416
                        }
                    }).save();
                })
                .then(function (navigation) {
                    return schema.Answer.Area({
                        area: [
                            {lat: 48.307537, lang: 16.325878},
                            {lat: 48.307300, lang: 16.325518},
                            {lat: 48.307044, lang: 16.325672},
                            {lat: 48.306828, lang: 16.325299},
                            {lat: 48.306563, lang: 16.325630},
                            {lat: 48.307253, lang: 16.326877}
                        ]
                    }).save()
                        .then(function (answer) {
                            return schema.Station({
                                name: "Station 2",
                                description: "This is Station 2",
                                answer: answer,
                                navigation: navigation
                            }).save();
                        });
                })
                .then(function (station) {
                    return cluster.addStation(station);
                })
                .then(function () {
                    console.log(JSON.stringify(cluster, null, 2));
                })
        })
        .catch(function (err) {
            throw err;
        });

    module.exports()
        .then(function () {
            return schema.User.createUser("rene.hollander@hotmail.de", "Rene8888", "1234", 'admin');
        })
        .then(function (user) {
            return schema.User.verifyToken(user.validationToken);
        })
        .then(function (user) {
            return schema.Team.createTeam('SuperTeam', user, "1234");
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
        })
        .then(function (data) {
            console.log(data);
        });
}

if (false) {
    module.exports()
        .then(function () {
            return schema.User.createUser("rene.hollander@hotmail.de", "Rene8888", "1234");
        })
        .then(function (user) {
            return schema.User.verifyToken(user.validationToken);
        });
}