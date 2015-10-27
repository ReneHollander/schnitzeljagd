"use strict";

var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var auth = require('./auth.js');
var cfg = require('./../cfg.js');
var app = require("./../app.js");

module.exports.init = function () {
    /*
     var onHeaders = require('on-headers');
     app.expressApp.use(function (req, res, next) {
     onHeaders(res, function () {
     if (!req.url.startsWith('/bower_components/') && !req.url.startsWith('/javascripts/') && !req.url.startsWith('/images/')) {
     console.log("sending headers for: " + req.url);
     }
     });
     next();
     });
     */
    // view engine setup
    app.expressApp.set('views', cfg.directory.view);
    app.expressApp.set('view engine', 'jade');

    app.expressApp.use(favicon(cfg.directory.favicon));
    //app.expressApp.use(logger('dev'));
    app.expressApp.use(bodyParser.json());
    app.expressApp.use(bodyParser.urlencoded({extended: true}));
    app.expressApp.use(cookieParser());
    app.expressApp.use(express.static(cfg.directory.public));

    auth.configureExpress(app.expressApp);

    setupRoutes();

    // catch 404 and forward to error handler
    app.expressApp.use(function (req, res, next) {
        var err = new Error('Not Found');
        err.status = 404;
        next(err);
    });

    // error handlers
    app.expressApp.use(function (err, req, res, next) {
        reportError(err, req, res);
    });

};

function renderSuccess(req, res, page, title, msg, data) {
    if (!data) data = {};
    data.success = {
        title: title,
        msg: msg
    };
    res.render(page, data);
}
module.exports.renderSuccess = renderSuccess;

function renderError(req, res, page, title, errortext, list, data) {
    if (!data) data = {};
    data.error = {
        title: title,
        errortext: msg,
        list: list
    };
    res.render(page, data);
}
module.exports.renderError = renderError;

function catchErrorMiddleware(req, res, page, title, errortext, data) {
    return function (err) {
        if (err instanceof Error) reportError(err, req, res);
        else {
            renderError(req, res, page, title, errortext, [err], data);
        }
    }
}
module.exports.catchErrorMiddleware = catchErrorMiddleware;

function successMiddleware(req, res, page, title, msg, data) {
    return function () {
        renderSuccess(req, res, page, title, msg, data);
    }
}
module.exports.successMiddleware = successMiddleware;

function reportError(err, req, res) {
    var status = err.status || 500;
    var color;
    var shortdesc;
    var longdesc;
    var stack;

    if (status == 404) {
        color = 'text-yellow';
        shortdesc = 'Oops! Page not found.';
        longdesc = 'We could not find the page you were looking for.';
        err = undefined;
    } else if (status == 403) {
        color = 'text-red';
        shortdesc = 'Access Forbidden!';
        longdesc = 'You are not allowed to access this page!';
        err = undefined;
    } else if (status >= 400 && status <= 499) {
        color = 'text-red';
        shortdesc = 'Oops! You fucked up.';
        if (err.status) {
            longdesc = 'I don\' know what you did, but it was not good: ' + err.message + '\nMaybe try again and punch yourself in the face?';
        } else {
            longdesc = 'I don\' know what you did, but it was not good. Maybe try again and punch yourself in the face?';
        }
    } else if (status >= 500 && status <= 599) {
        color = 'text-red';
        shortdesc = 'Oops! I fucked up.';
        if (err.status) {
            longdesc = 'I don\' know what I did that broke all the things: ' + err.message + '\nMaybe write an angry email to the admin?';
        } else {
            longdesc = 'I don\' know what I did that broke all the things. Maybe write an angry email to the admin?';
        }
    } else {
        color = 'text-red';
        shortdesc = 'Oops! I don\'t know what fucked up.';
        if (err.status) {
            longdesc = 'I don\' know what fucked up and who caused the fuck up: ' + err.message + '\nMaybe write an angry email to the admin or punch yourself in the face for your own stupidity?';
        } else {
            longdesc = 'I don\' know what fucked up and who caused the fuck up. Maybe write an angry email to the admin or punch yourself in the face for your own stupidity?';
        }
    }

    if (err && err.stack && app.expressApp.get('env') === 'development') {
        stack = err.stack;
    }
    if (req.user) {
        res.render('error', {
            stack: stack,
            color: color,
            shortdesc: shortdesc,
            longdesc: longdesc,
            status: status,
            user: req.user
        });
    } else {
        res.end(JSON.stringify({
            stack: stack,
            color: color,
            shortdesc: shortdesc,
            longdesc: longdesc,
            status: status
        }));
    }

    if (err) throw err;
}

module.exports.reportError = reportError;

function setupRoutes() {
    function loadRoute(name) {
        return require(path.join(cfg.directory.routes, name));
    }

    // login and registration
    app.expressApp.use('/login', loadRoute('login'));
    app.expressApp.use('/register', loadRoute('register'));

    // Pages for Users and Admins
    app.expressApp.use('/', loadRoute('index'));
    app.expressApp.use('/team', loadRoute('team'));

    // Admin pages
    app.expressApp.use('/admin/map', loadRoute('admin/map'));
    app.expressApp.use('/admin/station', loadRoute('admin/station'));
}