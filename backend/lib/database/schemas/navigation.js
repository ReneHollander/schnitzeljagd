var Promise = require('bluebird');
var gravatar = require('gravatar');
var mongoose = Promise.promisifyAll(require('mongoose'));
var bcrypt = Promise.promisifyAll(require('bcrypt'));
var ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = function (schemas) {
    var options = {
        discriminatorKey: 'type'
    };

    var BaseSchema = new mongoose.Schema({
        text: String
    }, options);
    var Base = mongoose.model('Navigation', BaseSchema);

    var CompassSchema = new mongoose.Schema({
        "target": {
            "lat": {
                type: Number,
                required: true
            },
            "lang": {
                type: Number,
                required: true
            }
        },
        "showdistance": {
            type: Boolean,
            default: true
        }
    }, options);
    var Compass = Base.discriminator('compass', CompassSchema);

    var MapSchema = new mongoose.Schema({
        "target": {
            "lat": {
                type: Number,
                required: true
            },
            "lang": {
                type: Number,
                required: true
            }
        }
    }, options);
    var Map = Base.discriminator('map', MapSchema);

    var TextSchema = new mongoose.Schema({
        content: {
            type: String,
            required: true
        }
    }, options);
    var Text = Base.discriminator('text', TextSchema);

    return {
        Base: Base,
        Compass: Compass,
        Map: Map,
        Text: Text
    };
};
