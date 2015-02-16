/*jshint strict:false */
/*global auth:false */
/*global React:false */
var Logout = React.createClass({
  componentDidMount: function () {
    auth.logout();
  },

  render: function () {
    $("#top").removeClass('header-dashboard').addClass('header');
    /* jshint ignore:start */
    return <p>You are now logged out</p>;
    /* jshint ignore:end */
  }
});
