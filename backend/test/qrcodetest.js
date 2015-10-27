var qrcode = require('qrcode');
var randomstring = require('randomstring');

qrcode.save('qr.png', randomstring.generate(6));
