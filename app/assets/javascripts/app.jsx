define(function(require){
  var React = require('react');
  var Router = require('react-router');
  var Login = require('jsx!auth/login');
  var Logout = require('jsx!auth/logout');
  var Dashboard = require('jsx!admin/dashboard');
  var User = require('jsx!user/user');
  var auth = require('auth/auth');

  var Route = Router.Route, DefaultRoute = Router.DefaultRoute,
    Link=Router.Link, RouteHandler = Router.RouteHandler,
    Redirect=Router.Redirect;


  var App = React.createClass({
    mixins: [Router.Navigation],

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
      return (
        <div>
          <RouteHandler/>
        </div>
      );
    }
  });

  App.init = function () {
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

  };

  return App;

});