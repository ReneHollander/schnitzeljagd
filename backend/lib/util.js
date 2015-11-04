var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var deepPopulate = require('mongoose-deep-populate')(mongoose);
var randomstring = require('randomstring');

module.exports.randomStringGenerator = function (chars) {
    return function () {
        return randomstring.generate(chars);
    };
};

module.exports.express = {
    onError: function (error) {
        if (error.syscall !== 'listen') {
            throw error;
        }

        var bind = typeof port === 'string'
            ? 'Pipe ' + port
            : 'Port ' + port;

        // handle specific listen errors with friendly messages
        switch (error.code) {
            case 'EACCES':
                console.error(bind + ' requires elevated privileges');
                process.exit(1);
                break;
            case 'EADDRINUSE':
                console.error(bind + ' is already in use');
                process.exit(1);
                break;
            default:
                throw error;
        }
    }
};

module.exports.normalizePort = function (val) {
    var port = parseInt(val, 10);
    if (isNaN(port)) {
        return val;
    }
    if (port >= 0) {
        return port;
    }
    return false;
};

module.exports.registerMongoosePlugins = function (schema) {
    schema.plugin(deepPopulate);
};

module.exports.promisePopulate = function (fields) {
    return function (data) {
        return data.populate(fields);
    }
};

module.exports.promiseDeepPopulate = function (fields) {
    return function (data) {
        return new Promise(function (resolve, reject) {
            data.deepPopulate(fields, function (err) {
                if (err) reject(err);
                else resolve(data);
            });
        });
    }
};