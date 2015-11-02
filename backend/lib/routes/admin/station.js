"use strict";

var Promise = require('bluebird');
var express = require('express');
var auth = require('./../../auth.js');
var webinterface = require('../../webinterface.js');
var schema = require('../../database/schema.js');

var router = express.Router();

router.get('/', auth.checkUserMiddleware('admin'), function (req, res, next) {
    schema.StationOrder.getOrder()
        .then(function (stations) {
            res.render('admin/station', {user: req.user, stations: stations});
        });
});

router.post('/', auth.checkUserMiddleware('admin'), function (req, res, next) {
    console.log(JSON.stringify(req.body, null, 2));
    schema.StationOrder.getOrder()
        .then(function (stations) {
            res.render('admin/station', {user: req.user, stations: stations});
        });
});

module.exports = router;
