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
            text: String
        }]
    }, options);
    var Question = Base.discriminator('question', QuestionSchema);

    var AreaSchema = new mongoose.Schema({
        timetostay: Number,
        showonmap: Boolean,
        area: [{
            lat: Number,
            lang: Number
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