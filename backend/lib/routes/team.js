"use strict";

var Promise = require('bluebird');
var express = require('express');
var auth = require('./../auth.js');
var webinterface = require('../webinterface.js');
var schema = require('../database/schema.js');

var router = express.Router();

router.get('/', auth.checkUserMiddleware('user', 'admin'), function (req, res, next) {
    req.user.getTeam().then(function (team) {
        res.render('team', {user: req.user, team: team});
    });
});

router.post('/', auth.checkUserMiddleware('user', 'admin'), function (req, res, next) {
    console.log(req.body);
    var errors = [];
    if (req.body.createteam !== undefined) {
        if (!req.body.teamname) errors.push("No team name supplied!");
        if (!req.body.teampassword1) errors.push("No password supplied!");
        if (req.body.teampassword1 && req.body.teampassword1 != req.body.teampassword2) errors.push("Passwords do not match!");
        if (errors.length != 0) {
            res.render('team', {
                error: {
                    title: 'Error creating team!',
                    errortext: 'creating team',
                    list: errors
                },
                user: req.user
            });
        } else {
            schema.Team.createTeam(req.body.teamname, req.user, req.body.teampassword1)
                .then(function (team) {
                    res.render('team', {
                        success: {
                            title: "Team creation successful!",
                            msg: "Your team was successfully created! You got one step closer to get those Schnitzels!"
                        },
                        user: req.user,
                        team: team
                    });
                }).catch(function (err) {
                    if (err instanceof Error) webinterface.reportError(err, req, res);
                    else res.render('team', {
                        error: {
                            title: 'Error creating team!',
                            errortext: 'creating team',
                            list: [err]
                        }, user: req.user
                    });
                });
        }
    } else if (req.body.deleteteam !== undefined) {
        req.user.getTeam()
            .then(function (team) {
                if (team) {
                    return team.deleteTeam()
                        .then(function () {
                            res.render('team', {
                                success: {
                                    title: "Successfully deleted team!",
                                    msg: "You successfully deleted your team!"
                                },
                                user: req.user
                            });
                        });
                } else {
                    res.render('team', {
                        error: {
                            title: 'Error deleting team!',
                            errortext: 'deleting team',
                            list: ['You are not in a team!']
                        },
                        user: req.user
                    });
                }
            });
    } else {
        req.user.getTeam().then(function (team) {
            res.render('team', {user: req.user, team: team});
        });
    }
});

module.exports = router;

