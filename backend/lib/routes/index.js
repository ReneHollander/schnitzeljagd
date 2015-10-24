"use strict";

var Promise = require('bluebird');
var express = require('express');
var auth = require('./../auth.js');
var webinterface = require('../webinterface.js');
var database = require('../database/');

var router = express.Router();

router.get('/', auth.checkUserMiddleware('user', 'admin'), function (req, res, next) {
    res.render('index', {user: req.user});
});

module.exports = router;
