/*jshint strict:false */
/*global React:false */
/*global Authentication:false */
var DashboardNav = React.createClass({
  mixins: [Authentication],

  render: function() {
    return (
              /* jshint ignore:start */
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
              <a href="/dashboard"><i className="fa fa-fw fa-dashboard"></i> Dashboard</a>
            </li>
            <li>
              <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i className="fa fa-fw fa-arrows-v"></i> Meta Models <i className="fa fa-fw fa-caret-down"></i></a>
              <ul id="demo" className="collapse">
                <li>
                  <a href="/dashboard/regions"><i className="fa fa-fw fa-table"></i> Regions</a>
                </li>
                <li>
                  <a href="#"><i className="fa fa-fw fa-table"></i> SubRegions</a>
                </li>
                <li>
                  <a href="#"><i className="fa fa-fw fa-table"></i> Sites</a>
                </li>
          </ul>

            </li>
          </ul>
        </div>
        </nav>
        /* jshint ignore:end */
    );
  }
});
