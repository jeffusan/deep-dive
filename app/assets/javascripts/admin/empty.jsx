define(function(require){

  var React = require('react');
  var ReactRouter = require('react-router');

  var Empty = React.createClass({

    mixins: [ReactRouter.State],

    render: function() {
      return (

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

      );
    }
  });

  return Empty;
});