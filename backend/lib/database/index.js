"use strict";

var Promise = require('bluebird');
var path = require('path');
var Datastore = require('nedb');
var fs = Promise.promisifyAll(require('fs-extra'));
var glob = Promise.promisify(require("glob"));
var cfg = require('../../cfg.js');

module.exports.init = function () {
    return fs.mkdirsAsync(cfg.directory.db).then(function () {
        return glob(path.join(__dirname, "collections", "*.js")).then(function (files) {
            var props = {};
            files.forEach(function (file) {
                var coll = require(file);
                var name = path.basename(file, path.extname(file));
                var datastore = Promise.promisifyAll(new Datastore({filename: path.join(cfg.directory.db, name + ".db")}));
                props[name] = datastore.loadDatabaseAsync()
                    .then(function () {
                        coll.datastore = datastore;
                        return Promise.resolve(coll.init(datastore))
                            .then(function () {
                                return coll;
                            });
                    });
            });
            return Promise.props(props).then(function (cllctns) {
                module.exports = cllctns;
            });
        });
    });
};
