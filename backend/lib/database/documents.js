"use strict";

var camo = require('camo');
var deepcopy = require('deepcopy');
var Document = camo.Document;
var EmbeddedDocument = camo.EmbeddedDocument;

function camoDocumentToJSON(values) {
    var ret = deepcopy(values);
    ret.id = ret._id;
    delete ret._id;
    return ret;
}

class CustomDocument extends Document {
    toJSON() {
        return camoDocumentToJSON(this._values);
    }
}

class CustomEmbeddedDocument extends EmbeddedDocument {
    toJSON() {
        return camoDocumentToJSON(this._values);
    }
}

class User extends CustomDocument {
    constructor() {
        super('users');

        this.email = String;
        this.username = {
            type: String,
            index: true
        };
        this.passwordHash = String;
        this.registrationDate = {
            type: Date,
            default: Date.now
        };
    }
}

module.exports.init = function () {
    module.exports.User = User;
};