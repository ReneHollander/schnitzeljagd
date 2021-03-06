"use strict";

var Promise = require('bluebird');
var express = require('express');
var passport = require('passport');
var auth = require('./../auth.js');
var webinterface = require('../webinterface.js');
var schema = require('../database/schema.js');

var router = express.Router();

router.get('/', function (req, res) {
    res.render('login');
});

router.post('/', function (req, res, next) {
    passport.authenticate('local', function (err, user, info) {
        if (err && err instanceof Error) return next(err);
        else {
            if (err || !user) return res.render('login', {error: [err]});
            else {
                req.logIn(user, function (err) {
                    if (err) return next(err);
                    else return res.redirect('/');
                });
            }
        }
    })(req, res, next);
});

module.exports = router;
