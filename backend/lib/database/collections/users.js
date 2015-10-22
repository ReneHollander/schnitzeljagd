var database = require('./../index.js');
var bcrypt = require('bcrypt');

var ds;
module.exports.init = function (datastore) {
    ds = datastore;

    return datastore.ensureIndexAsync({fieldName: 'email', unique: true}).then(function () {
        return datastore.ensureIndexAsync({fieldName: 'username', unique: true});
    })
};

module.exports.createUser = function (email, username, password) {
    // todo duplicate user names and emails
    return new Promise(function (resolve, reject) {
        bcrypt.hash(password, 8, function (err, hash) {
            if (err) reject(err);
            else resolve(hash);
        });
    }).then(function (hash) {
            return ds.insertAsync({
                email: email,
                username: username,
                passwordHash: hash
            });
        });
};

