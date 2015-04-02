define(function(require) {
  'use strict';
  var React = require('react');
  var auth = require('auth/auth');

  var EditableTextInput = React.createClass({

    componentDidMount: function() {
      $('#' + this.props.elementid).editable({
        type: 'text',
        pk: this.props.id,
        url: this.props.url,
        ajaxOptions: {
          headers: {
            'X-XSRF-TOKEN': auth.getToken()
          },
          'dataType': 'json',
          'contentType': 'application/json'
        },
        params: function(params) { return JSON.stringify(params); }
      });
    },

    render: function() {
      return (
        <a className="col" href="#" id={this.props.elementid} ref="input">{this.props.name}</a>
      );
    }
  });
  return EditableTextInput;
});
