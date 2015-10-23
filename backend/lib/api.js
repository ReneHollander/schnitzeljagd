var socketio = require('socket.io');
var socketioauth = require('socketio-auth');
var auth = require('./auth.js');
var app = require('./../app.js');

var io;
var userIO;
var adminIO;

module.exports.init = function () {
    io = socketio(app.httpServer);
    userIO = io.of('/user');
    adminIO = io.of('/admin');

    auth.configureIO(adminIO);

    socketioauth(userIO, {
        authenticate: function (socket, authData, callback) {
            console.log("name: " + authData.name);
            console.log("password: " + authData.password);
            auth.authenticate(authData.name, authData.password, function (err, data) {
                if (err) return callback(err, false);
                if (!data) return callback(new Error("an unknown error occured"), false);
                return callback(null, true);
            });
        }
    });

    userIO.on('connection', function (socket) {
        console.log('connected');

        socket.on('ping', function (data) {
            socket.emit("pong");
            console.log("recieved ping");
        });

    });
};

module.exports.getIO = function () {
    return io;
};

module.exports.getUserIO = function () {
    return userIO;
};

module.exports.getAdminIO = function () {
    return adminIO;
};