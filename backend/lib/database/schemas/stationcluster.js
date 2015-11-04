var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;
var util = require('../../util.js');


module.exports = function (schemas) {
    var schema = mongoose.Schema({
        name: {
            type: String,
            required: true
        },
        description: {
            type: String
        },
        stations: [{
            type: ObjectId,
            ref: 'Station'
        }]
    });
    util.registerMongoosePlugins(schema);

    schema.statics.createCluster = function (name, description, stations) {
        return new schemas.StationCluster({
            name: name,
            description: description,
            stations: stations
        }).save().then(util.promiseDeepPopulate(['stations', 'stations.navigation', 'stations.answer']));
    };

    schema.statics.get = function (id) {
        return this.find({_id: id}).deepPopulate(['stations.navigation', 'stations.answer']);
    };

    schema.methods.getStations = function () {
        return this.stations;
    };

    schema.methods.setStationOrder = function (order) {
        this.stations = order;
    };

    schema.methods.addStation = function (station) {
        this.stations.push(station);
        return this.save().then(util.promiseDeepPopulate(['stations.navigation', 'stations.answer']));
    };

    return mongoose.model('StationCluster', schema);
};
