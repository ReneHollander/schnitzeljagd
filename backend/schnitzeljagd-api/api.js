var socketio = require('socket.io');
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var socketioauth = require('socketio-auth');

function API(httpServer, auth) {
    this.io = socketio(httpServer);

    this.io.on('connection', function (socket) {
        console.log('connected');
        socket.emit('testEvent', {hello: "world"});
    });


    this.userIO = this.io.of('/user');
    this.adminIO = this.io.of('/admin');

    this.adminIO.use(passportSocketIo.authorize({
        cookieParser: cookieParser,
        key: auth.getKey(),       // the name of the cookie where express/connect stores its session_id
        secret: auth.getSecret(),    // the session_secret to parse the cookie
        store: auth.getSessionStore()
    }));

    socketioauth(this.userIO, {
        authenticate: function (socket, data, callback) {
            console.log(callback);
            console.log("uuid: " + data.uuid);
            auth.authenticateUUID(data.uuid, function (err, data) {
                if (err) return callback(err);
                if (!data) return callback(new Error("an unknown error occured"));
                if (data == false) {
                    return callback(null, false);
                } else if (data == true) {
                    return callback(null, true);
                } else {
                    socket.userdata = data;
                    return callback(null, true);
                }
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