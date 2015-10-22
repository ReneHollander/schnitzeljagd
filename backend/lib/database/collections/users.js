var database = require('./../index.js');
var bcrypt = require('bcrypt');
var Promise = require('bluebird');
var randomstring = require("randomstring");

var ds;
module.exports.init = function (datastore) {
    ds = datastore;

    return datastore.ensureIndexAsync({fieldName: 'email', unique: true})
        .then(function () {
            return datastore.ensureIndexAsync({fieldName: 'username', unique: true});
        })
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