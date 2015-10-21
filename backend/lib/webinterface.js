"use strict";

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
    //app.expressApp.use(logger('dev'));
    app.expressApp.use(bodyParser.json());
    app.expressApp.use(bodyParser.urlencoded({extended: true}));
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
            var status = err.status || 500;
            var color;
            var shortdesc;
            var longdesc;
            if (status == 404) {
                color = 'text-yellow';
                shortdesc = 'Oops! Page not found.';
                longdesc = 'We could not find the page you were looking for.';
            } else if (status >= 400 && status <= 499) {
                color = 'text-red';
                shortdesc = 'Oops! You fucked up.';
                if (err.status) {
                    longdesc = 'I don\' know what you did, but it was not good: ' + err.message + '<br>Maybe try again and punch yourself in the face?';
                } else {
                    longdesc = 'I don\' know what you did, but it was not good. Maybe try again and punch yourself in the face?';
                }
            } else if (status >= 500 && status <= 599) {
                color = 'text-red';
                shortdesc = 'Oops! I fucked up.';
                if (err.status) {
                    longdesc = 'I don\' know what I did that broke all the things: ' + err.message + '<br>Maybe write an angry email to the admin?';
                } else {
                    longdesc = 'I don\' know what I did that broke all the things. Maybe write an angry email to the admin?';
                }
            } else {
                color = 'text-red';
                shortdesc = 'Oops! I don\'t know what fucked up.';
                if (err.status) {
                    longdesc = 'I don\' know what fucked up and who caused the fuck up: ' + err.message + '<br>Maybe write an angry email to the admin or punch yourself in the face for your own stupidity?';
                } else {
                    longdesc = 'I don\' know what fucked up and who caused the fuck up. Maybe write an angry email to the admin or punch yourself in the face for your own stupidity?';
                }
            }
            res.render('error', {
                stack: err.stack,
                color: color,
                shortdesc: shortdesc,
                longdesc: longdesc,
                status: status
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

module.exports.displayErrorPage = function (res, req) {
};

function setupRoutes() {
    function loadRoute(name) {
        return require(path.join(cfg.directory.routes, name));
    }

    app.expressApp.use('/', loadRoute('index'));
    app.expressApp.use('/login', loadRoute('login'));
    app.expressApp.use('/register', loadRoute('register'));
    app.expressApp.use('/map', loadRoute('map'));
}