var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var cfg = require('./../cfg.js');
var app = require("./../app.js");

module.exports.init = function () {
    // view engine setup
    app.expressApp.set('views', cfg.directory.view);
    app.expressApp.set('view engine', 'jade');

    // uncomment after placing your favicon in /public
    app.expressApp.use(favicon(cfg.directory.favicon));
    app.expressApp.use(logger('dev'));
    app.expressApp.use(bodyParser.json());
    app.expressApp.use(bodyParser.urlencoded({extended: false}));
    app.expressApp.use(cookieParser());
    app.expressApp.use(express.static(cfg.directory.public));
    /*
     app.expressApp.use(auth.getExpressSession({
     secret: auth.getSecret(),
     key: auth.getKey(),
     store: auth.getSessionStore(),
     resave: true,
     saveUninitialized: true,
     proxy: true
     }));
     app.expressApp.use(auth.getPassport().initialize());
     */

    setupRoutes();

    // catch 404 and forward to error handler
    app.expressApp.use(function (req, res, next) {
        var err = new Error('Not Found');
        err.status = 404;
        next(err);
    });

    // error handlers

    // development error handler
    // will print stacktrace
    if (app.expressApp.get('env') === 'development') {
        app.expressApp.use(function (err, req, res, next) {
            res.status(err.status || 500);
            res.render('error', {
                message: err.message,
                error: err
            });
        });
    }

    // production error handler
    // no stacktraces leaked to user
    app.expressApp.use(function (err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: {}
        });
    });
};

function setupRoutes() {
    function loadRoute(name) {
        return require(path.join(cfg.directory.routes, name));
    }

    app.expressApp.use('/', loadRoute('index'));
    app.expressApp.use('/map', loadRoute('map'));
}