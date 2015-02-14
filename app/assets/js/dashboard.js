/*global ReactRouter:false */
/*global React:false */
/*global auth:false */
/*global Login:false */
var Router = ReactRouter;

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
  mixins: [ Authentication, Router.State ],

  render: function () {

    var makeTheSausage = function(selection) {
      /* jshint ignore:start */

      if(! selection) {
        return <Empty/>;

      } else {
        var page;
        switch(selection) {

        case 'regions': page = <Regions/>; break;
        default: page = <Empty/>; break;

        }
        return page;

      }
      /* jshint ignore:end */
      };

    return (
      /* jshint ignore:start */
        <div id="wrapper">
          <DashboardNav/>
          {makeTheSausage(this.getParams().selection)}
      </div>
        /* jshint ignore:end */
      );
  }
});
