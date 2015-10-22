"use strict";

var express = require('express');
var router = express.Router();
var Promise = require('bluebird');
var webinterface = require('../webinterface.js');
var database = require('../database/');
var mail = require('../mail.js');

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('register');
});

router.post('/', function (req, res, next) {
    if (!req.body) throw new Error("No request body found!");

    var errors = [];
    if (!req.body.email) errors.push("No email supplied!");
    if (!req.body.username) errors.push("No username supplied!");
    if (!req.body.password1) errors.push("No password supplied!");
    if (req.body.password1 && req.body.password1 != req.body.password2) errors.push("Passwords do not match!");
    if (errors.length != 0) {
        res.render('register', {error: errors});
    } else {
        database.users.createUser(req.body.email, req.body.username, req.body.password1)
            .then(function (user) {
                console.log(user);
                return mail.send(user.email, 'Aktivierung deines Accounts', "Bitte nutze den folgenden Link um deine E-Mail Adresse zu verifizieren: " + user.validationToken, "Bitte nutze den folgenden Link um deine E-Mail Adresse zu verifizieren: " + user.validationToken)
                    .then(function () {
                        res.render('register', {success: true});
                    });
            })
            .catch(function (err) {
                if (err instanceof Error) webinterface.reportError(err, res);
                else res.render('register', {error: [err]});
            });
    }
});

module.exports = router;
