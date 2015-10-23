var Promise = require('bluebird');

module.exports.ensureIndices = function (datastore, indices) {
    return Promise.map(indices, function (index) {
        return datastore.ensureIndexAsync(index);
    });
};