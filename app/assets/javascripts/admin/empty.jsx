define(function(require){
  'use strict';

  var React = require('react');
  var ReactRouter = require('react-router');

  var Empty = React.createClass({

    mixins: [ReactRouter.State],

    render: function() {
      return (
        <div className="container-fluid">
          <div className="panel panel-default">
            <div className="panel-heading clearfix">
              <h3 className="panel-title pull-left">Home Page</h3>
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

  return Empty;
});