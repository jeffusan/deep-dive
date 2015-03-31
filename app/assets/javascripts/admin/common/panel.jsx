define(function(require){

  var React = require('react');

  var Panel = React.createClass({

    render: function() {
      return (
        <div className="container-fluid">
          <div className="panel panel-default">
            <div className="panel-heading clearfix">
              <h3 className="panel-title pull-left">{this.props.name}</h3>
            </div>
            <div className="panel-body">
            </div>
            <div className="panel-footer">
            </div>
          </div>
        </div>
      );
    }
  });

  return Panel;
});