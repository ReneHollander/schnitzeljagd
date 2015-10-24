"use strict";

var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var _ = require('underscore');
var schema = require('./database/schema.js');
var webinterface = require('./webinterface.js');

var secret = crypto.randomBytes(64).toString('hex');
var key = "schnitzeljagd.sid";

var sessionStore = new expresssession.MemoryStore();
var authStrategy = new passportlocal.Strategy({
    usernameField: 'email',
    passwordField: 'password'
}, authenticate);

passport.use('local', authStrategy);
passport.serializeUser(function (user, done) {
    done(null, user.id);
});
passport.deserializeUser(function (id, done) {
    schema.User.findOne({id: id}).then(function (user) {
        console.log(user);
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
        secret: secret,
        store: sessionStore,
        key: key,
        resave: true,
        saveUninitialized: true
    }));
    express.use(passport.initialize());
    express.use(passport.session());
};

module.exports.configureIO = function (io) {
    io.use(passportSocketIo.authorize({
        cookieParser: cookieParser,
        key: key,
        secret: secret,
        store: sessionStore
    }));
};

function authenticate(email, password, done) {
    schema.User.validateUser(email, password)
        .then(function (user) {
            done(null, user);
        }).catch(function (err) {
            done(err);
        })
}
module.exports.authenticate = authenticate;