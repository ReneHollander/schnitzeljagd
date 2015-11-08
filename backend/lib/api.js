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
        if (err instanceof Error) {
            cb({error: err.message});
            throw err;
        }
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
                    return team.nextStation()
                        .then(function (station) {
                            var data = station.toJSON();
                            delete data.__v;
                            delete data.navigation._id;
                            delete data.navigation.__v;
                            delete data.answer._id;
                            delete data.answer.__v;
                            if (data.answer.type === 'qr') {
                                delete data.answer.token;
                            }
                            ack(data);
                        });
                    /*
                     if (!team.currentstation) {
                     team.nextStation()
                     .then(function (station) {
                     ack(station);
                     })
                     .catch(catchErrorMiddleware(ack));
                     } else {
                     ack(team.currentstation);
                     }
                     */
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