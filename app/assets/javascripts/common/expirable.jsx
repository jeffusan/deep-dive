define(function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var Well = ReactBootstrap.Well;

  var Expire = React.createClass({

    getDefaultProps: function(){
      return {delay: 1000};
    },

    getInitialState: function(){
      return {visible: true};
    },

    componentWillReceiveProps: function(nextProps){
      // reset the timer if children are changed
      if (nextProps.children !== this.props.children) {
        this.setTimer();
        this.setState({visible: true});
      }
    },
    componentDidMount: function(){
      this.setTimer();
    },
    setTimer: function(){
      // clear any existing timer
      this._timer != null ? clearTimeout(this._timer) : null;

      // hide after `delay` milliseconds
      this._timer = setTimeout(function(){
      this.setState({visible: false});
      this._timer = null;
      }.bind(this), this.props.delay);
    },
    render: function(){
      return this.state.visible
           ? <Well>{this.props.children}</Well>
           : <span />;
    }
  });

  return Expire;
});