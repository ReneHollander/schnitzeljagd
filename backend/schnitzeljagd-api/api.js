var socketio = require('socket.io');
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var socketioauth = require('socketio-auth');

function API(httpServer, auth) {
    this.io = socketio(httpServer);

    this.io.on('connection', function (socket) {
        console.log('connected');
        socket.emit('testEvent', {hello: "world"});

        socket.on('lol', function (data, callback) {
            console.log('lol: ' + data);
            callback({request: data});
        });

    });

    this.userIO = this.io.of('/user');
    this.adminIO = this.io.of('/admin');

    this.adminIO.use(passportSocketIo.authorize({
        cookieParser: cookieParser,
        key: auth.getKey(),
        secret: auth.getSecret(),
        store: auth.getSessionStore()
    }));

    socketioauth(this.userIO, {
        authenticate: function (socket, authData, callback) {
            console.log(callback);
            console.log("name: " + authData.name);
            console.log("password: " + authData.password);
            auth.authenticate(authData.name, authData.password, function (err, data) {
                if (err) return callback(err, false);
                if (!data) return callback(new Error("an unknown error occured"), false);
                return callback(null, true);
            });
        }
    });
}

API.prototype.getUserIO = function () {
    return this.userIO;
};

API.prototype.getAdminIO = function () {
    return this.adminIO;
};

module.exports = API;
