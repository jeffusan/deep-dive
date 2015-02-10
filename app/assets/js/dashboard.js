var Router = ReactRouter;

var Dashboard = React.createClass({
  mixins: [ Authentication, Router.State ],

  render: function () {

    var makeTheSausage = function(selection) {

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

      };

      return (
        <div id="wrapper">
          <DashboardNav/>
          {makeTheSausage(this.getParams().selection)}
      </div>
      );
  }
});
