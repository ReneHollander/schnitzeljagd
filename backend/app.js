var express = require('express');
var debug = require('debug')('backend:server');
var http = require('http');

var Auth = require('./auth.js');
var Webinterface = require('./schnitzeljagd-webinterface/webinterface.js');
var API = require('./schnitzeljagd-api/api.js');

var expressApp = express();

var port = normalizePort(process.env.PORT || '3000');
expressApp.set('port', port);

var server = http.createServer(expressApp);

server.on('error', function (error) {
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
});
server.on('listening', function () {
    var addr = server.address();
    var bind = typeof addr === 'string'
        ? 'pipe ' + addr
        : 'port ' + addr.port;
    debug('Listening on ' + bind);
});
server.listen(port);

var auth = new Auth();
var api = new API(server, auth);
var webinterface = new Webinterface(expressApp, api, auth);


function normalizePort(val) {
    var port = parseInt(val, 10);
    if (isNaN(port)) {
        return val;
    }
    if (port >= 0) {
        return port;
    }
    return false;
}
