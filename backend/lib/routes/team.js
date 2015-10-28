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
        if (!req.body.name) errors.push("No team name supplied!");
        if (!req.body.password1) errors.push("No password supplied!");
        if (req.body.password1 && req.body.password1 != req.body.password2) errors.push("Passwords do not match!");
        if (errors.length != 0) {
            webinterface.renderSuccess(req, res, 'team', 'Error creating team!', 'creating team', {user: req.user});
        } else {
            schema.Team.createTeam(req.body.name, req.user, req.body.password1)
                .then(function (team) {
                    webinterface.renderSuccess(req, res, 'team', "Team creation successful!", "Your team was successfully created! You got one step closer to get those Schnitzels!", {
                        user: req.user,
                        team: team
                    });
                })
                .catch(webinterface.catchErrorMiddleware(req, res, 'team', 'Error creating team!', 'creating team', {user: req.user}));
        }
    }
    else if (req.body.deleteteam !== undefined) {
        req.user.getTeam()
            .then(function (team) {
                if (team) {
                    return team.deleteTeam();
                } else {
                    return Promise.reject('You are not in a team!');
                }
            })
            .then(webinterface.successMiddleware(req, res, 'team', "Successfully deleted team!", "You successfully deleted your team!", {user: req.user}))
            .catch(webinterface.catchErrorMiddleware(req, res, 'team', 'Error deleting team!', 'deleting team', {user: req.user}));
    } else if (req.body.removemember !== undefined) {
        req.user.getTeam()
            .then(function (team) {
                return schema.User.getUserById(req.body.userid)
                    .then(function (user) {
                        return team.removeMember(user);
                    })
                    .then(function (team2) {
                        console.log(team2);
                        webinterface.renderSuccess(req, res, 'team', 'Successfully deleted member', 'The team member was successfully deleted from your team!', {
                            user: req.user,
                            team: team2
                        });
                    })
                    .catch(webinterface.catchErrorMiddleware(req, res, 'team', 'Error removing team member!', 'removing team member', {
                        user: req.user,
                        team: team
                    }));
            });
    } else {
        req.user.getTeam().then(function (team) {
            res.render('team', {user: req.user, team: team});
        });
    }
})
;

module.exports = router;

