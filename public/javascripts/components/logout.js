/*jshint strict:false */
/*global auth:false */
/*global React:false */
/*global ReactRouter:false*/
/*global $:false*/
var Logout = React.createClass({

  mixins: [ReactRouter.Navigation],

  componentDidMount: function () {
    auth.logout();
    $.setTimeout(this.replaceWith('/login'), 4000);
  },

  render: function () {
    $("#top").removeClass('header-dashboard').addClass('header');
    /* jshint ignore:start */
    return <p>You are now logged out</p>;
    /* jshint ignore:end */
  }
});
