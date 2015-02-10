var Router = ReactRouter;
var Route = Router.Route, DefaultRoute = Router.DefaultRoute,
    Link=Router.Link, RouteHandler = Router.RouteHandler,
    Redirect=Router.Redirect;

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
    var loginOrOut = this.state.loggedIn ?
          <div></div> :
          <Login/>;

    return (
        <div>{loginOrOut}
        <RouteHandler/>
        </div>
    );
  }
});
