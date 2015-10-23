"use strict";

var Promise = require('bluebird');
var express = require('express');
var auth = require('./../auth.js');
var webinterface = require('../webinterface.js');
var database = require('../database/');

var router = express.Router();

/* GET home page. */
router.get('/', auth.checkUserLoggedIn, function (req, res, next) {
    res.render('index');
});

module.exports = router;
