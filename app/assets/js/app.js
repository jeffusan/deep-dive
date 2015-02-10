var Router = ReactRouter;
var Route = Router.Route, DefaultRoute = Router.DefaultRoute,
  Link=Router.Link, RouteHandler = Router.RouteHandler;

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

    function roleOrNot(state) {
      if(state.loggedIn) {
        if(state.admin) {
          return <Dashboard/>;
        } else {
          return <User/>;
        }
      } else {
        return <Login/>;
      }
    };
    return (
        <div>{roleOrNot(this.state)}
        <RouteHandler/>
        </div>
    );
  }
});

var Dashboard = React.createClass({
  mixins: [ Authentication ],

  render: function () {
    var token = auth.getToken();
    return (
        <div id="wrapper">
           <nav className="navbar navbar-inverse navbar-fixed-top" role="navigation">
               <div className="navbar-header">
                <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span className="sr-only">Toggle navigation</span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                </button>
                <a className="navbar-brand" href="index.html">SB Admin</a>
            </div>
           <ul className="nav navbar-right top-nav">
          <li className="dropdown">
            <a href="#" className="dropdown-toggle" data-toggle="dropdown"><i className="fa fa-user"></i> John Smith <b className="caret"></b></a>
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
              <a href="index.html"><i className="fa fa-fw fa-dashboard"></i> Dashboard</a>
            </li>
            <li>
              <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i className="fa fa-fw fa-arrows-v"></i> Dropdown <i className="fa fa-fw fa-caret-down"></i></a>
              <ul id="demo" className="collapse">
                <li>
                  <a href="#">Dropdown Item</a>
                </li>
                <li>
                  <a href="#">Dropdown Item</a>
                </li>
               </ul>
            </li>
          </ul>
        </div>
        </nav>
        <div id="page-wrapper">
            <div className="container-fluid">
                <div className="row">
                    <div className="col-lg-12">
                        <h1 className="page-header">
                            Blank Page
                            <small>Subheading</small>
                        </h1>
                        <ol className="breadcrumb">
                            <li>
                                <i className="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                            </li>
                            <li className="active">
                                <i className="fa fa-file"></i> Blank Page
                            </li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
      </div>
    );
  }
});
