var Router = ReactRouter;
var Route = Router.Route, DefaultRoute = Router.DefaultRoute,
  Link=Router.Link, RouteHandler = Router.RouteHandler;

var App = React.createClass({
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
    auth.login();
  },

  render: function () {

    function showPage(state) {

      if(state.loggedIn) {
        if(state.admin) {
          return <Dashboard/>;
        } else {
          return <User/>;
        }
      } else {
        return <Login/>;
      }
    }

    return (
        <p>{showPage(this.state)}</p>
    );
  }
});

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

var Dashboard = React.createClass({
  mixins: [ Authentication ],

  render: function () {
    var token = auth.getToken();
    return (
      <div class="dd-well">
        <h1>Dashboard</h1>
        <p>You made it!</p>
        <p>{token}</p>
      </div>
    );
  }
});

var User = React.createClass({
  mixins: [Authentication],

  render: function() {
    var token = auth.getToken();

    return (
      <div class="dd-well">
        <h1>User page</h1>
        <p>You made it!</p>
        <p>{token}</p>
      </div>
    );

  }
});

var Login = React.createClass({
  mixins: [ Router.Navigation ],

  statics: {
    attemptedTransition: null
  },

  getInitialState: function () {
    return {
      error: false
    };
  },

  handleSubmit: function (event) {
    event.preventDefault();
    var email = this.refs.email.getDOMNode().value;
    var pass = this.refs.pass.getDOMNode().value;
    auth.login(email, pass, function (loggedIn) {
      if (!loggedIn)
        return this.setState({ error: true });

      if (Login.attemptedTransition) {
        var transition = Login.attemptedTransition;
        Login.attemptedTransition = null;
        transition.retry();
      } else {
        this.replaceWith('/about');
      }
    }.bind(this));
  },

  render: function () {
    var errors = this.state.error ? <p>Bad login information</p> : '';
    return (
        <div id="loginModal" className="modal show" tabIndex="-1" role="dialog" aria-hidden="true">
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h1 className="text-center">Login</h1>
              </div>
              <div className="modal-body">
                <form onSubmit={this.handleSubmit} className="form col-md-12 center-block">
                  <div className="form-group">
                    <input ref="email" className="form-control input-lg" placeholder="email" defaultValue="bigman@atware.jp"/>
                  </div>
                  <div className="form-group">
                    <input ref="pass" className="form-control input-lg" placeholder="Password"/>
                  </div>
                  <div className="form-group">
                    <button className="btn btn-primary btn-lg btn-block">Sign In</button>
                    <span className="pull-right"><a href="#">Register</a></span><span><a href="#">Need help?</a></span>
                  </div>
                 {errors}
               </form>
              </div>
              <div className="modal-footer">
                <div className="col-md-12">
                  <button className="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                </div>
              </div>
            </div>
          </div>
        </div>
    );
  }
});

var About = React.createClass({
  render: function () {
    return <h1>About</h1>;
  }
});

var Logout = React.createClass({
  componentDidMount: function () {
    auth.logout();
  },

  render: function () {
    return <p>You are now logged out</p>;
  }
});



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
          localStorage.user = res.user;
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

  getToken: function () {
    return localStorage.token;
  },

  logout: function (cb) {
    delete localStorage.token;
    delete localStorage.user;
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
    if(localStorage.user !== undefined && localStorage.user.roles !== undefined) {
      return $.inArray("administrator", localStorage.user.roles) > -1;
    } else {
      return false;
    }

  },

  getUser: function() {
    return localStorage.user;
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
        token: data.authToken,
        user: data.user
      });},
    'error': function(data) {
      cb({
        authenticated: false
      });
    }
  });

};

var routes = (
  <Route handler={App}>
    <Route name="login" handler={Login}/>
    <Route name="logout" handler={Logout}/>
    <Route name="about" handler={About}/>
    <Route name="dashboard" handler={Dashboard}/>
    <Route name="user" handler={User}/>
  </Route>
);

Router.run(routes, function (Handler) {
  React.render(<Handler/>, document.getElementById('magic'));
});
