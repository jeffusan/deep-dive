define(function(require){

  var React = require('react');
  var Router = require('react-router');
  var Link = Router.Link;
  var Authentication = require('auth/authentication');
  var auth = require('auth/auth');

  var User = React.createClass({
    mixins: [Authentication],

    render: function() {
      var token = auth.getToken();
      $( "#wrapper" ).remove();
      return (

      <div className="dd-well">
        <h1>User page</h1>
        <p>You made it!</p>
        <p>{token}</p>
        <Link to="logout">Log out</Link>
        </div>

      );
    }
  });

  return User;
});