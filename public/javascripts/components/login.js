/*jshint strict:false */
/*global React:false */
/*global ReactRouter:false */
/*global auth:false */
/*global ReactBootstrap:false */

var Input = ReactBootstrap.Input;

var Login = React.createClass({
  mixins: [ ReactRouter.Navigation ],

  statics: {
    attemptedTransition: null
  },

  getInitialState: function () {
    return {
      error: false
    };
  },

  handleSubmit: function (event) {
    event.preventDefault();
    var email = this.refs.email.getDOMNode().value;
    var pass = this.refs.pass.getDOMNode().value;
    auth.login(email, pass, function (loggedIn) {
      if (!loggedIn) {
        return this.setState({ error: true });
      }

      if (Login.attemptedTransition) {
        var transition = Login.attemptedTransition;
        Login.attemptedTransition = null;
        transition.retry();
      } else {
        if(auth.isAdmin()) {
          console.log("Looks like an admin");
          this.replaceWith('/dashboard');
        } else if(auth.loggedIn()) {
          console.log("Looks logged in");
          this.replaceWith('/user');
        } else {
          console.log("Ooops");
          this.replaceWith('/login');
        }
      }
    }.bind(this));
  },

  render: function () {
    /* jshint ignore:start */
    var errors = this.state.error ? <p>Bad login information</p> : '';
    return (

        <div className="container">
        <div className="row">
        <div className="col-md-offset-5 col-md-3">
        <div className="form-login">
        <h4>Welcome back.</h4>
        <input type="email" ref="email" className="form-control input-sm chat-input" placeholder="email" defaultValue="bigman@atware.jp"/>
         <br />
        <input ref="pass" type="password" className="form-control input-sm chat-input" placeholder="password" />
        <br />
        <div className="wrapper">
          <span className="group-btn">
          <Button type="button" onClick={this.handleSubmit} bsStyle="primary" className="btn btn-primary btn-md">login <i className="fa fa-sign-in"></i></Button>
          </span>
           {errors}
          </div>
        </div>
        </div>
        </div>
        </div>

    );
    /* jshint ignore:end */
  }
});
