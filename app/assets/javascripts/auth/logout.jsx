define(function(require) {

  var React = require('react');
  var ReactRouter = require('react-router');

  var Logout = React.createClass({

    mixins: [ReactRouter.Navigation],

    componentDidMount: function () {
      auth.logout();
      $.setTimeout(this.replaceWith('/login'), 4000);
    },

    render: function () {
      $("#top").removeClass('header-dashboard').addClass('header');
      return <p>You are now logged out</p>;
    }
  });

  return Logout;

});