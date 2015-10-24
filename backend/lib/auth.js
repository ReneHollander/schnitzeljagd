var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var database = require('./database/');
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
    done(null, user);
});
passport.deserializeUser(function (user, done) {
    done(null, user);
});

function checkRole(user, allowedRoles, callback) {
    if (user) {
        if (user.validation_link) {
            callback(new Error("Please validate your E-Mail address before logging in!"));
            return;
        }
        for (allowedRole of allowedRoles) {
            if (user.role == allowedRole) {
                callback(null, true);
                return;
            }
        }
        callback(null, false);
    } else {
        callback(new Error("Please log in before!"));
    }
}
module.exports.checkRole = checkRole;

function checkUserExpress(allowedRoles, req, res, next) {
    checkRole(req.user, allowedRoles, function (err, result) {
        if (err) res.render('login', {error: [err.message]});
        else if (result === false) {
            var forbidden = new Error('Forbidden');
            forbidden.status = 403;
            next(forbidden);
        }
        else if (result === true) next();
        else throw new Error('Unknown state!');
    });
}

module.exports.checkUserMiddleware = function () {
    var args = arguments;
    return function (req, res, next) {
        checkUserExpress(args, req, res, next);
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
    database.users.validateUser(email, password)
        .then(function (user) {
            done(null, user);
        }).catch(function (err) {
            done(err);
        })
}
module.exports.authenticate = authenticate;