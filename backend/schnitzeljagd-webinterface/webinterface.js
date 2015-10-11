var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

function Webinterface(expressApp, api, auth) {
    this.expressApp = expressApp;
    this.api = api;

    // view engine setup
    this.getExpressApp().set('views', path.join(__dirname, 'views'));
    this.getExpressApp().set('view engine', 'jade');

    // uncomment after placing your favicon in /public
    this.getExpressApp().use(favicon(path.join(__dirname, 'public', 'favicon.png')));
    this.getExpressApp().use(logger('dev'));
    this.getExpressApp().use(bodyParser.json());
    this.getExpressApp().use(bodyParser.urlencoded({extended: false}));
    this.getExpressApp().use(cookieParser());
    this.getExpressApp().use(express.static(path.join(__dirname, 'public')));
    /*
    this.getExpressApp().use(auth.getExpressSession({
        secret: auth.getSecret(),
        key: auth.getKey(),
        store: auth.getSessionStore(),
        resave: true,
        saveUninitialized: true,
        proxy: true
    }));
    this.getExpressApp().use(auth.getPassport().initialize());
    */

    var instance = this;
    this.getExpressApp().use(function (req, res, next) {
        req.webinterface = instance;
        next();
    });

    this.setupRoutes();

    // catch 404 and forward to error handler
    this.getExpressApp().use(function (req, res, next) {
        var err = new Error('Not Found');
        err.status = 404;
        next(err);
    });

    // error handlers

    // development error handler
    // will print stacktrace
    if (this.getExpressApp().get('env') === 'development') {
        this.getExpressApp().use(function (err, req, res, next) {
            res.status(err.status || 500);
            res.render('error', {
                message: err.message,
                error: err
            });
        });
    }

    // production error handler
    // no stacktraces leaked to user
    this.getExpressApp().use(function (err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: {}
        });
    });
}

Webinterface.prototype.setupRoutes = function () {
    this.getExpressApp().use('/', require('./routes/index'));
    this.getExpressApp().use('/map', require('./routes/map'));
};

Webinterface.prototype.getAPI = function () {
    return this.api;
};

Webinterface.prototype.getExpressApp = function () {
    return this.expressApp;
};

module.exports = Webinterface;