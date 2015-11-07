var socketio = require('socket.io');
var socketioauth = require('socketio-auth');
var auth = require('./auth.js');
var app = require('./../app.js');
var schema = require('./database/schema.js');

var io;
var userIO;
var adminIO;

function catchErrorMiddleware(cb) {
    return function (err) {
        if (err instanceof Error) cb({error: err.message});
        else cb({error: err});
    }
}

module.exports.init = function () {
    io = socketio(app.httpServer);
    userIO = io.of('/user');
    adminIO = io.of('/admin');

    auth.configureIO(adminIO);

    socketioauth(userIO, {
        authenticate: function (socket, authData, callback) {
            console.log("email: " + authData.email);
            console.log("password: " + authData.password);
            auth.authenticate(authData.email, authData.password, function (err, data) {
                if (err) return callback(err, false);
                if (!data) return callback(new Error("an unknown error occured"), false);
                return callback(null, true);
            });
        },
        postAuthenticate: function (socket, data) {
            socket.client.getUser = function () {
                return schema.User.getUserByEmail(data.email)
                    .catch(function (err) {
                        socket.disconnect('unauthorized');
                    });
            };
            socket.client.getTeam = function () {
                return socket.client.getUser()
                    .then(function (user) {
                        return user.getTeam();
                    })
            };
        }
    });

    userIO.on('connection', function (socket) {
        console.log('connected');

        socket.on('get_current_station', function (ack) {
            socket.client.getTeam()
                .then(function (team) {
                    ack(team.currentstation);
                })
                .catch(catchErrorMiddleware(ack));
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