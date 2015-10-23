var express = require('express');
var router = express.Router();
var passport = require('passport');

/* GET home page. */
router.get('/', function (req, res) {
    res.render('login');
});

router.post('/', function (req, res, next) {
    passport.authenticate('local', function (err, user, info) {
        if (err && err instanceof Error) return next(err);
        if (err || !user) return res.render('login', {error: [info]});
        req.logIn(user, function (err) {
            if (err) return next(err);
            return res.redirect('/');
        });
    })(req, res, next);
});

module.exports = router;
