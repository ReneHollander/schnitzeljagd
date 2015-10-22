var nodemailer = require('nodemailer');
var Promise = require('bluebird');
var cfg = require('../cfg.js');

var transporter = Promise.promisifyAll(nodemailer.createTransport(cfg.mail.transport));

module.exports.send = function (to, subject, text, html) {
    var mailOptions = {
        from: cfg.mail.from,
        to: to,
        subject: subject,
        text: text,
        html: html
    };
    return transporter.sendMailAsync(mailOptions);
};