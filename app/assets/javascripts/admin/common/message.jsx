define(function(require) {

  var React = require('react');

  var Message = React.createClass({
    getInitialState : function() {
      return { message: ''};
    },

    render: function() {

      var display = function() {
        if(this.state.message !== '') {
          if(!!this.state.message.error) {
            return <div className="bg-success">{JSON.stringify(this.state.message.value, null, ' ')}</div>;
          } else {
            return <div className="bg-danger">{JSON.stringify(this.state.message.error.value, null, ' ')}</div>;
          }
        } else {
          return <div></div>;
        }
      };

      return (
        <div>{display}</div>
      );
    }
  });

  return Message;
});
