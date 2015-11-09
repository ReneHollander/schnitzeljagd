"use strict";

var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');
var MongoDBStore = require('connect-mongodb-session')(expresssession);
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var _ = require('underscore');
var schema = require('./database/schema.js');
var webinterface = require('./webinterface.js');
var cfg = require('./../cfg.js');

/*var sessionStore = new expresssession.MemoryStore();*/
var sessionStore = new MongoDBStore({
    uri: cfg.database.connectionstring,
    collection: 'sessions'
});

var authStrategy = new passportlocal.Strategy({
    usernameField: 'email',
    passwordField: 'password'
}, authenticate);

passport.use('local', authStrategy);
passport.serializeUser(function (user, done) {
    done(null, user._id);
});
passport.deserializeUser(function (_id, done) {
    schema.User.findOne({_id: _id}).then(function (user) {
        done(null, user);
    }).catch(function (err) {
        done(err);
    });
});

module.exports.checkUserMiddleware = function () {
    var allowedRoles = arguments;
    return function (req, res, next) {
        if (req.user) {
            var allowed = false;
            _.each(allowedRoles, function (allowedRole) {
                if (req.user.role == allowedRole) {
                    allowed = true;
                }
            });
            if (allowed) {
                next();
            } else {
                var forbidden = new Error('Forbidden');
                forbidden.status = 403;
                next(forbidden);
            }
        } else {
            res.render('login', {error: ["Please log in before!"]});
        }
    }
};

module.exports.configureExpress = function (express) {
    express.use(expresssession({
        secret: cfg.auth.secret,
        store: sessionStore,
        key: cfg.auth.key,
        resave: true,
        saveUninitialized: true,
        cookie: {
            maxAge: 1000 * 60 * 60 * 24 * 7 // 1 week
        }
    }));
    express.use(passport.initialize());
    express.use(passport.session());
};

module.exports.configureIO = function (io) {
    io.use(passportSocketIo.authorize({
        cookieParser: cookieParser,
        key: cfg.auth.key,
        secret: cfg.auth.secret,
        store: sessionStore,
        cookie: {
            maxAge: 1000 * 60 * 60 * 24 * 7 // 1 week
        }
    }));
};

function authenticate(email, password, done) {
    schema.User.validateUser(email, password)
        .then(function (user) {
            done(null, user);
        }).catch(function (err) {
            done(err);
        });
}
module.exports.authenticate = authenticate;