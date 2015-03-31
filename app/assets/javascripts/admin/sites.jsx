define(function(require){

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');

  var AddSite = React.createClass({
    render: function() {
      return (
      <div>Hello</div>
      );
    }

  });

  var Sites = React.createClass({

    render: function() {
      return (
        <div className="container-fluid">
          <div className="panel panel-default">
            <div className="panel-heading clearfix">
              <h3 className="panel-title pull-left">Sites</h3>
            </div>
            <div className="panel-body">
            <AddSite/>
            </div>
            <div className="panel-footer">
            </div>
          </div>
        </div>

      );
    }
  });

  return Sites;
});