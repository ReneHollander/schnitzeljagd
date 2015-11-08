var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;
var util = require('../../util.js');

function randomInt(low, high) {
    return Math.floor(Math.random() * (high - low) + low);
}

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

    schema.statics.nextCluster = function (completedClusters) {
        return schemas.StationCluster.find({_id: {$nin: completedClusters}}).count()
            .then(function (cnt) {
                return schemas.StationCluster.find({_id: {$nin: completedClusters}}).limit(-1).skip(randomInt(0, cnt))
                    .then(function (docs) {
                        return docs.pop();
                    })
            });
    };

    schema.statics.currentCluster = function (currentStation) {
        return this.find({stations: currentStation._id}).deepPopulate(['stations.navigation', 'stations.answer'])
    };

    schema.statics.nextStation = function (current, completedClusters) {
        var that = this;
        if (!current) {
            return that.nextCluster(completedClusters)
                .then(function (cluster) {
                    console.log(cluster);
                    return cluster.nextStation(current);
                })
        } else {
            return that.currentCluster(current)
                .then(function (cluster) {
                    return cluster.nextStation(current)
                        .then(function (station) {
                            if (station) return station;
                            else {
                                completedClusters.push(cluster);
                                return that.nextCluster(completedClusters)
                                    .then(function (newcluster) {
                                        if (!newcluster) return undefined;
                                        else return newcluster.nextStation(current);
                                    });
                            }
                        });
                });
        }
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

    schema.methods.getStationByIndex = function (index) {
        return util.promiseDeepPopulate(['stations', 'stations.navigation', 'stations.answer'])(this)
            .then(function (doc) {
                return doc.stations[index];
            })
    };

    schema.methods.nextStation = function (station) {
        if (station) {
            var currindex = this.stations.indexOf(station);
            if (currindex == -1) return Promise.reject(new Error('Station not in cluster'));
            if (this.stations.length - 1 == currindex) return Promise.resolve(undefined);
            else return this.getStationByIndex(currindex + 1);
        } else {
            return this.getStationByIndex(0);
        }
    };

    return mongoose.model('StationCluster', schema);
};
