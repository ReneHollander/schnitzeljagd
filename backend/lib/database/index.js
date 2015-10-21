"use strict";

var Promise = require('bluebird');
var camo = require('camo');
var bcrypt = require('bcrypt');
var fs = Promise.promisifyAll(require('fs-extra'));
var cfg = require('../../cfg.js');
var documents = require('./documents.js');

module.exports.getClient = camo.getClient;

module.exports.init = function () {
    return fs.mkdirsAsync(cfg.directory.db).then(function () {
        return camo.connect("nedb://" + cfg.directory.db).then(function () {
            documents.init();
            module.exports.documents = documents;
        });
    });
};

module.exports.createUser = function (email, username, password) {
    // todo duplicate user names and emails
    return new Promise(function (resolve, reject) {
        bcrypt.hash(password, 8, function (err, hash) {
            if (err) reject(err);
            else resolve(hash);
        });
    }).then(function (hash) {
            return documents.User.create({
                email: email,
                username: username,
                passwordHash: hash
            }).save();
        });
};