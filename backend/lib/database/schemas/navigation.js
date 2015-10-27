var Promise = require('bluebird');
var gravatar = require('gravatar');
var mongoose = Promise.promisifyAll(require('mongoose'));
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var options = {
        discriminatorKey: 'type'
    };

    var BaseSchema = new mongoose.Schema({}, options);
    var Base = mongoose.model('Navigation', BaseSchema);

    var CompassSchema = new mongoose.Schema({
        "target": {
            "lat": Number,
            "lang": Number
        },
        "showdistance": Boolean
    }, options);
    var Compass = Base.discriminator('compass', CompassSchema);

    var MapSchema = new mongoose.Schema({
        "target": {
            "lat": Number,
            "lang": Number
        }
    }, options);
    var Map = Base.discriminator('map', MapSchema);

    var TextSchema = new mongoose.Schema({
        content: String
    }, options);
    var Text = Base.discriminator('text', TextSchema);

    return {
        Base: Base,
        Compass: Compass,
        Map: Map,
        Text: Text
    };
};
