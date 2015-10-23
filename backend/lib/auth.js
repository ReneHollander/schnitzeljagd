var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');
var passportSocketIo = require("passport.socketio");
var cookieParser = require('cookie-parser');
var database = require('./database/');

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

module.exports.checkUserLoggedIn = function (req, res, next) {
    if (req.user) {
        next();
    } else {
        res.redirect('/login');
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