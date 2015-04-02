define(function(require){
  'use strict';

  var Login = require('jsx!auth/login');
  var auth = require('auth/auth');

  var Authentication = {
    statics: {
      willTransitionTo: function (transition) {
        if (!auth.loggedIn()) {
          Login.attemptedTransition = transition;
          transition.redirect('/login');
        }
      }
    }
  };
});
