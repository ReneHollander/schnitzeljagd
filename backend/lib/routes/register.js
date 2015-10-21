"use strict";

var express = require('express');
var router = express.Router();
var database = require('../database/');

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
        database.createUser(req.body.email, req.body.username, req.body.password1).then(function () {
            // TODO send validatio email
            res.render('register', {success: true});
        }).catch(function (err) {
            throw err;
        });
    }
});

module.exports = router;
