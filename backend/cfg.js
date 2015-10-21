var path = require('path');

module.exports.directory = {
    view: path.join(__dirname, 'views'),
    favicon: path.join(__dirname, 'public', 'favicon.png'),
    public: path.join(__dirname, 'public'),
    routes: path.join(__dirname, 'lib', 'routes'),
    data: path.join(__dirname, 'data'),
    db: path.join(__dirname, 'data', 'db')
};