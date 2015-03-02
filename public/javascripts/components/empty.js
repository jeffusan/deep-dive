/*jshint strict:false */
/*global React:false */
/*global ReactRouter:false */
var Router = ReactRouter;

var Empty = React.createClass({

  mixins: [Router.State],

  render: function() {
    return (
      /* jshint ignore:start */
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
                <li><div>{this.getParams().selection}</div></li>
               </ol>
             </div>
           </div>
         </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
