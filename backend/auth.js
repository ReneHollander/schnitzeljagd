var passport = require('passport');
var passportlocal = require('passport-local');
var crypto = require('crypto');
var expresssession = require('express-session');

var sidKey = 'schnitzeljagd.sid';

function Auth() {
    var instance = this;
    this.secret = crypto.randomBytes(64).toString('hex');
    this.sessionStore = new expresssession.MemoryStore();
    this.authStrategy = new passportlocal.Strategy(this.authenticateNameAndPassword);

    passport.use(this.authStrategy);
    passport.serializeUser(function (user, done) {
        done(null, user);
    });
    passport.deserializeUser(function (user, done) {
        done(null, user);
    });
}

Auth.prototype.getSecret = function () {
    return this.secret;
};

Auth.prototype.getKey = function () {
    return this.key;
};

Auth.prototype.getSessionStore = function () {
    return this.sessionStore;
};

Auth.prototype.getPassport = function () {
    return passport;
};

Auth.prototype.getExpressSession = function () {
    return expresssession;
};

Auth.prototype.getAuthStrategy = function () {
    return this.authStrategy;
};

Auth.prototype.authenticateNameAndPassword = function (username, password, done) {
    return done(null, {username: username, role: "admin"});
};

Auth.prototype.authenticateUUID = function (uuid, done) {
    return done(null, {teamname: "team 1", role: "user"});
};


module.exports = Auth;