var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');

var secret = crypto.randomBytes(64).toString('hex');
var sessionStore = new expresssession.MemoryStore();
var authStrategy = new passportlocal.Strategy(authenticate);
var key = "schnitzeljagd.sid";

passport.use(authStrategy);
passport.serializeUser(function (user, done) {
    done(null, user);
});
passport.deserializeUser(function (user, done) {
    done(null, user);
});

module.exports.getSecret = function () {
    return secret;
};

module.exports.getKey = function () {
    return key;
};

module.exports.getSessionStore = function () {
    return sessionStore;
};

module.exports.getPassport = function () {
    return passport;
};

module.exports.getExpressSession = function () {
    return expresssession;
};

module.exports.getAuthStrategy = function () {
    return authStrategy;
};

function authenticate(name, password, done) {
    return done(null, {teamname: name, role: "user"});
}
module.exports.authenticate = authenticate;