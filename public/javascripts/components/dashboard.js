/*global ReactRouter:false */
/*global React:false */
/*global auth:false */
/*global Login:false */
/*global $:false */
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
        case 'subregions': page = <SubRegions/>; break;
        default: page = <Empty/>; break;

        }
        return page;

      }
      /* jshint ignore:end */
      };

    $("#top").removeClass('header')
    $("body").addClass('header-dashboard');
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
