"use strict";

var Promise = require('bluebird');
var express = require('express');
var auth = require('./../../auth.js');
var webinterface = require('../../webinterface.js');
var database = require('../../database/init');

var router = express.Router();

router.get('/', auth.checkUserMiddleware('admin'), function (req, res, next) {
    res.render('admin/station', {user: req.user});
});

module.exports = router;
