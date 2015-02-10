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


var auth = {
  login: function (email, pass, cb) {
    cb = arguments[arguments.length - 1];
    if (localStorage.token) {
      if (cb) cb(true);
      this.onChange({
        isLoggedIn: true,
        isAdmin: this.isAdmin()
      });
      return;
    }
    if(email !== undefined && pass !== undefined) {
      makeRequest(email, pass, function (res) {
        if (res.authenticated) {
          localStorage.token = res.token;
          localStorage.username = res.user.name;
          localStorage.roles = res.user.roles;
          if (cb) cb(true);
          this.onChange({
            isLoggedIn: true,
            isAdmin: $.inArray("administrator", res.user.roles) > -1
          });
        } else {
          if (cb) cb(false);
          this.onChange({
            isLoggedIn: false,
            isAdmin: false
          });
        }
      }.bind(this));
    }
  },

  getUserName() {
    return localStorage.username;
  },

  getToken: function () {
    return localStorage.token;
  },

  logout: function (cb) {
    delete localStorage.token;
    delete localStorage.username;
    delete localStorage.roles;
    if (cb) cb();
    this.onChange({
      isLoggedIn: false,
      isAdmin: false
    });
  },

  loggedIn: function () {
    return !!localStorage.token;
  },

  isAdmin: function() {
    return $.inArray("administrator", [localStorage.roles]) > -1;
  },

  onChange: function () {}
};

function makeRequest(email, pass, cb) {

  $.ajax({
    'type': 'POST',
    'url': '/login',
    'contentType': 'application/json',
    'data': JSON.stringify({'email': email, 'password': pass}),
    'dataType': 'json',
    'async': false,
    'success': function(data) {
      cb({
        authenticated: true,
        token: data.token,
        user: data.user
      });},
    'error': function(data) {
      cb({
        authenticated: false
      });
    }
  });

};
