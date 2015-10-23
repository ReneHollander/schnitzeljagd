var bcrypt = require('bcrypt');
var Promise = require('bluebird');
var randomstring = require("randomstring");
var database = require('./../index.js');
var dbutil = require("./../util.js");

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
            return new Promise(function (resolve, reject) {
                bcrypt.hash(password, 8, function (err, hash) {
                    if (err) reject(err);
                    else resolve(hash);
                });
            }).then(function (hash) {
                    return ds.insertAsync({
                        email: email,
                        username: username,
                        passwordHash: hash,
                        registrationDate: new Date(),
                        validationToken: randomstring.generate(32)
                    });
                });
        } else {
            return Promise.reject("E-Mail or Username already in use!");
        }
    });
}
module.exports.createUser = createUser;

function verifyToken(validationToken) {
    return ds.findAsync({validationToken: validationToken})
        .then(function (docs) {
            if (docs.length == 0) {
                return Promise.reject("Invalid Verification Token or Verification Token already used!");
            } else {
                return ds.updateAsync({validationToken: validationToken}, {$set: {validationToken: undefined}})
                    .then(function () {
                        return true;
                    });
            }
        });
}
module.exports.verifyToken = verifyToken;