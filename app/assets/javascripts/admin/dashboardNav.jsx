define(function(require){
  var React = require('react');
  var Authentication = require('auth/authentication');
  var auth = require('auth/auth');
  var Router = require('react-router');
  var Link = Router.Link;

  var DashboardNav = React.createClass({
    mixins: [Authentication],

    render: function() {
      return (

      <nav className="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div className="navbar-header">
          <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span className="sr-only">Toggle navigation</span>
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
          </button>
          <a className="navbar-brand" href="index.html">Deep Dive!</a>
       </div>
        <ul className="nav navbar-right top-nav">
          <li className="dropdown">
            <a href="#" className="dropdown-toggle" data-toggle="dropdown"><i className="fa fa-user"></i> {auth.getUserName()} <b className="caret"></b></a>
            <ul className="dropdown-menu">
              <li>
                <a href="#"><i className="fa fa-fw fa-user"></i> Profile</a>
              </li>
              <li>
                <a href="#"><i className="fa fa-fw fa-envelope"></i> Inbox</a>
              </li>
              <li>
                <a href="#"><i className="fa fa-fw fa-gear"></i> Settings</a>
              </li>
              <li className="divider"></li>
              <li>
                <Link to="logout"><i className="fa fa-fw fa-power-off"></i> Log out</Link>
              </li>
            </ul>
           </li>
          </ul>
        <div className="collapse navbar-collapse navbar-ex1-collapse">
          <ul className="nav navbar-nav side-nav">
            <li>
              <a href="/dashboard"><i className="fa fa-fw fa-heartbeat"></i> Dashboard</a>
            </li>
            <li>
              <a href="/dashboard/users"><i className="fa fa-fw fa-database"></i> Users</a>
            </li>
            <li>
              <a href="/dashboard/regions"><i className="fa fa-fw fa-database"></i> Regions</a>
            </li>
            <li>
              <a href="/dashboard/subregions"><i className="fa fa-fw fa-database"></i> Subregions</a>
            </li>
            <li>
              <a href="/dashboard/sites"><i className="fa fa-fw fa-database"></i> Sites</a>
            </li>
            <li>
              <a href="/dashboard/reeftypes"><i className="fa fa-fw fa-table"></i> Reef Types</a>
            </li>
            <li>
              <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i className="fa fa-fw fa-arrows-v"></i> Benthic <i className="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo" className="collapse">
                  <li>
                    <a href="/dashboard/upload"><i className="fa fa-fw fa-upload"></i> Upload Benthic Data</a>
                  </li>
                  <li>
                    <a href="/dashboard/surveyevents"><i className="fa fa-fw fa-table"></i> Survey Events</a>
                  </li>
                  <li>
                    <a href="/dashboard/benthiccategories"><i className="fa fa-fw fa-table"></i> Categories</a>
                  </li>
                  <li>
                    <a href="/dashboard/benthicsubcategories"><i className="fa fa-fw fa-table"></i> Sub Categories</a>
                  </li>
                </ul>
           </li>
            <li>
              <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i className="fa fa-fw fa-arrows-v"></i> Survey Metadata <i className="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo" className="collapse">
                  <li>
                    <a href="/dashboard/surveyevents"><i className="fa fa-fw fa-table"></i> Benthic Surveys</a>
                  </li>
                </ul>
            </li>
          </ul>
        </div>
        </nav>

      );
    }
  });

  return DashboardNav;
});