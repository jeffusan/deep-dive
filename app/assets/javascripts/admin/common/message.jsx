define(function(require) {
  'use strict';

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var Well = ReactBootstrap.Well;

  var Message = React.createClass({

    getInitialState: function() {
      return {visible: true};
    },

    getDefaultProps: function(){
      return {delay: 8000};
    },

    componentDidMount: function() {
      this.setTimer();
    },

    setTimer: function() {
      // clear any existing timer
      this._timer != null ? clearTimeout(this._timer) : null;

      // hide after `delay` milliseconds
      this._timer = setTimeout(function(){
      this.setState({visible: false});
      this._timer = null;
      }.bind(this), this.props.delay);
    },

    render: function() {
      return this.state.visible ?
        <Well>{this.props.message}</Well> :
        <span/>;
    }
  });

  return Message;
});
