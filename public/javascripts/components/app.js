/*global React:false */
/*global ReactRouter:false */
/*global localStorage:false */
/*global $:false */
/*global Login:false */
/*global Dashboard:false */
/*global User:false */
/*global Logout:false */
var Router = ReactRouter;
var Route = Router.Route, DefaultRoute = Router.DefaultRoute,
    Link=Router.Link, RouteHandler = Router.RouteHandler,
    Redirect=Router.Redirect;

var auth = {

  ping: function(cb) {
    cb = arguments[arguments.length - 1];
    makePingRequest( function(res) {

      if(res.authenticated) {
          localStorage.token = res.token;
          localStorage.username = res.user.name;
          localStorage.roles = res.user.roles;
          if (cb) {cb(true);}
          this.onChange({
            isLoggedIn: true,
            isAdmin: $.inArray("administrator", res.user.roles) > -1
          });
      } else {
          if (cb) {
            cb(false);
          }
          this.onChange({
            isLoggedIn: false,
            isAdmin: false
          });
      }

    }.bind(this));
  },

  login: function (email, pass, cb) {
    cb = arguments[arguments.length - 1];
    if(email !== undefined && pass !== undefined) {
      makeRequest(email, pass, function (res) {
        if (res.authenticated) {
          localStorage.token = res.token;
          localStorage.username = res.user.name;
          localStorage.roles = res.user.roles;
          if (cb) {cb(true);}
          this.onChange({
            isLoggedIn: true,
            isAdmin: $.inArray("administrator", res.user.roles) > -1
          });
        } else {
          if (cb) {
            cb(false);
          }
          this.onChange({
            isLoggedIn: false,
            isAdmin: false
          });
        }
      }.bind(this));
    }
  },

  getUserName: function() {
    return localStorage.username;
  },

  getToken: function () {
    return localStorage.token;
  },

  hasToken: function() {
    return !!localStorage.token;
  },

  logout: function (cb) {
    delete localStorage.token;
    delete localStorage.username;
    delete localStorage.roles;
    if (cb) {cb();}
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

function makePingRequest(cb) {

  $.ajax({
    'type': 'GET',
    'url': '/authuser',
    'contentType': 'application/json',
    'async': false,
    'headers': {
      'X-XSRF-TOKEN': auth.getToken()
    },
    statusCode: {
      401:function(data) {
        console.log("I got this one");
        cb({
          authenticated: false
        });
      }
    },
    'success': function(data) {
      cb({
        authenticated: true,
        token: data.token,
        user: data.user
      });
    },
    'error': function(data) {
      cb({
        authenticated: false
      });
    }
  });
}

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

}

var App = React.createClass({

  mixins: [ReactRouter.Navigation],

  getInitialState: function () {
    return {
      loggedIn: auth.loggedIn()
    };
  },

  setStateOnAuth: function (data) {
    this.setState({
      loggedIn: data.isLoggedIn,
      admin: data.isAdmin
    });
  },

  componentWillMount: function () {
    auth.onChange = this.setStateOnAuth;
    if(auth.hasToken()) {
      auth.ping(function (loggedIn) {
        if(!loggedIn) {
          console.log("Not logged in!");
          auth.logout();
          this.replaceWith('/login');
        }

      }.bind(this));
    } else {
      this.replaceWith('/login');
    }
  },

  render: function () {
    /* jshint ignore:start */
    return (
        <div>
        <RouteHandler/>
        </div>
    );
    /* jshint ignore:end */
  }
});
  /* jshint ignore:start */
var routes = (
  <Route path="/" handler={App}>
    <Route name="login" handler={Login}/>
    <Route name="logout" handler={Logout}/>
    <Route name="dashboard" path="dashboard/?:selection?" handler={Dashboard}/>
    <Route name="user" path="user/:selection" handler={User}/>
  </Route>
);

Router.run(routes, function (Handler, state) {
  React.render(<Handler/>, document.getElementById('magic'));
});
/* jshint ignore:end */
