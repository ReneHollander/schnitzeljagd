var Promise = require('bluebird');
var util = require('../../util.js');
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
    var Base = mongoose.model('Answer', BaseSchema);

    var QRSchema = new mongoose.Schema({
        token: {
            type: String,
            default: util.randomStringGenerator(7)
        }
    }, options);
    var QR = Base.discriminator('qr', QRSchema);

    var QuestionSchema = new mongoose.Schema({
        answers: [{
            _id: {
                type: ObjectId,
                default: ObjectId
            },
            text: {
                type: String,
                required: true
            }
        }]
    }, options);
    var Question = Base.discriminator('question', QuestionSchema);

    var AreaSchema = new mongoose.Schema({
        timetostay: {
            type: Number,
            default: 30
        },
        showonmap: {
            type: Boolean,
            default: true
        },
        area: [{
            lat: {
                type: Number,
                required: true
            },
            lang: {
                type: Number,
                required: true
            }
        }]
    }, options);
    var Area = Base.discriminator('area', AreaSchema);

    return {
        Base: Base,
        QR: QR,
        Question: Question,
        Area: Area
    };
};