var Promise = require('bluebird');
var nodemailer = require('nodemailer');
var htmlToText = require('nodemailer-html-to-text').htmlToText;
var fs = Promise.promisifyAll(require('fs-extra'));
var glob = require("glob");
var jade = require('jade');
var path = require('path');
var cfg = require('../cfg.js');

var transporter = Promise.promisifyAll(nodemailer.createTransport(cfg.mail.transport));
transporter.use('compile', htmlToText());

module.exports.send = function (to, subject, jadeFile, data) {
    var mailOptions = {
        from: cfg.mail.from,
        to: to,
        subject: subject,
        html: jade.renderFile(path.join(cfg.directory.mail.templates, jadeFile + '.jade'), data)
    };

    return transporter.sendMailAsync(mailOptions);
};