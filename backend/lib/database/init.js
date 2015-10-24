"use strict";

var Promise = require('bluebird');
var path = require('path');
var fs = Promise.promisifyAll(require('fs-extra'));
var cfg = require('../../cfg.js');
var mongoose = Promise.promisifyAll(require('mongoose'));

module.exports = function () {
    return mongoose.connectAsync('mongodb://localhost/schnitzeljagd');
};
