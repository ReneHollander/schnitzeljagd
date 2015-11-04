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

    schema.statics.nextStation = function (current, completedClusters) {
        return this.find({stations: current._id}).deepPopulate(['stations.navigation', 'stations.answer'])
            .then(function (cluster) {
                return cluster.nextStation(current);
            })
            .then(function (station) {
                if (station) return station;
                else {
                    // todo find random station that was not already completed
                }
            });
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

    schema.methods.nextStation = function (station) {
        if (station) {
            var currindex = this.stations.indexOf(station);
            if (currindex == -1) return Promise.reject(new Error('Station not in cluster'));
            if (this.stations.length - 1 == currindex) return Promise.resolve(undefined);
            else return this.stations[currindex + 1].populate(['navigation', 'answer']);
        } else {
            return this.stations[0].populate(['navigation', 'answer']);
        }
    };

    return mongoose.model('StationCluster', schema);
};
