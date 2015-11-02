var Promise = require('bluebird');
var mongoose = Promise.promisifyAll(require('mongoose'));
var gravatar = require('gravatar');
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;
var util = require('../../util.js');


module.exports = function (schemas) {
    var schema = mongoose.Schema({
        stations: [{
            type: ObjectId,
            ref: 'Station'
        }]
    });
    util.registerMongoosePlugins(schema);

    function model() {
        return schemas.StationOrder;
    }

    function getOrderDocument() {
        return model().find({})
            .deepPopulate(['stations', 'stations.navigation', 'stations.answer'])
            .then(function (docs) {
                if (docs.length == 0) {
                    return new schemas.StationOrder({}).save();
                } else if (docs.length == 1) {
                    return docs[0];
                } else {
                    return Promise.reject(new Error('There should only be one StationOrder document in the database'));
                }
            })
    }

    function getOrder() {
        return getOrderDocument()
            .then(function (doc) {
                return doc.stations;
            });
    }

    function setOrder(order) {
        return getOrderDocument()
            .then(function (doc) {
                doc.stations = order;
            });
    }

    function addStation(station) {
        return getOrderDocument()
            .then(function (doc) {
                doc.stations.push(station);
                return doc.save().then(util.promiseDeepPopulate(['stations', 'stations.navigation', 'stations.answer']));
            });
    }

    schema.statics.getOrderDocument = getOrderDocument;
    schema.statics.getOrder = getOrder;
    schema.statics.getStations = getOrder;
    schema.statics.setOrder = setOrder;
    schema.statics.addStation = addStation;

    return mongoose.model('StationOrder', schema);
};
