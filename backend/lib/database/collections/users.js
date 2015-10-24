var Promise = require('bluebird');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var randomstring = require("randomstring");
var database = require('./../index.js');
var gravatar = require('gravatar');
var dbutil = require("./../../util/database.js");

var ds;
module.exports.init = function (datastore) {
    ds = datastore;
    return dbutil.ensureIndices(datastore, [
        {fieldName: 'email', unique: true},
        {fieldName: 'username', unique: true},
        {fieldName: 'validationToken', unique: true}
    ]);
};

function createUser(email, username, password) {
    return ds.findAsync({
        $or: [
            {username: username},
            {email: email}
        ]
    }).then(function (docs) {
        if (docs.length == 0) {
            return bcrypt.hashAsync(password, 8)
                .then(function (hash) {
                    return ds.insertAsync({
                        email: email,
                        username: username,
                        passwordHash: hash,
                        registrationDate: new Date(),
                        validationToken: randomstring.generate(32),
                        role: 'user',
                        profilepicture: {
                            s64: gravatar.url(email, {s: 64, r: 'pg', d: '404'}),
                            s128: gravatar.url(email, {s: 128, r: 'pg', d: '404'}),
                            s256: gravatar.url(email, {s: 256, r: 'pg', d: '404'}),
                            s512: gravatar.url(email, {s: 512, r: 'pg', d: '404'})
                        }
                    });
                });
        } else {
            return Promise.reject("E-Mail or Username already in use");
        }
    });
}
module.exports.createUser = createUser;

function verifyToken(validationToken) {
    return ds.findAsync({validationToken: validationToken})
        .then(function (docs) {
            if (docs.length == 0) {
                return Promise.reject("Invalid Verification Token or Verification Token already used");
            } else {
                return ds.updateAsync({validationToken: validationToken}, {$set: {validationToken: undefined}})
                    .then(function () {
                        return true;
                    });
            }
        });
}
module.exports.verifyToken = verifyToken;

function getUserByEmail(email) {
    return ds.findAsync({email: email})
        .then(function (docs) {
            if (docs.length == 0) {
                return Promise.reject("No user with given E-Mail found");
            } else {
                return docs.pop();
            }
        });
}
module.exports.getUserByEmail = getUserByEmail;

function validateUser(email, password) {
    return getUserByEmail(email)
        .then(function (user) {
            return bcrypt.compareAsync(password, user.passwordHash)
                .then(function (res) {
                    if (res == false) {
                        return Promise.reject("Invalid password");
                    } else {
                        return user;
                    }
                });
        });
}
module.exports.validateUser = validateUser;