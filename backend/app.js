var express = require('express');
var http = require('http');
var util = require('./lib/util.js');
var dbinit = require('./lib/database/init.js');
var api = require('./lib/api.js');
var webinterface = require('./lib/webinterface.js');

var expressApp = express();

var port = util.normalizePort(process.env.PORT || '3000');
expressApp.set('port', port);

var httpServer = http.createServer(expressApp);

httpServer.on('error', util.express.onError);
httpServer.on('listening', function () {
    var addr = httpServer.address();
    var bind = typeof addr === 'string' ? 'pipe ' + addr : 'port ' + addr.port;
    console.log('Listening on ' + bind);

    dbinit()
        .then(api.init)
        .then(webinterface.init)
        .catch(function (err) {
            throw err;
        })
    ;
});
httpServer.listen(port);


module.exports.expressApp = expressApp;
module.exports.httpServer = httpServer;