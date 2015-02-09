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
      if (!loggedIn)
        return this.setState({ error: true });

      if (Login.attemptedTransition) {
        var transition = Login.attemptedTransition;
        Login.attemptedTransition = null;
        transition.retry();
      } else {
        console.log("oops!");
        this.replaceWith('/');
      }
    }.bind(this));
  },

  render: function () {
    var errors = this.state.error ? <p>Bad login information</p> : '';
    return (
        <div className="container">
        <div className="row">
        <div className="col-md-offset-5 col-md-3">
        <div className="form-login">
        <h4>Welcome back.</h4>
        <input ref="email" type="email" className="form-control input-sm chat-input" placeholder="email" defaultValue="bigman@atware.jp"/>
         <br />
        <input ref="pass" type="password" className="form-control input-sm chat-input" placeholder="password" />
        <br />
        <div className="wrapper">
          <span className="group-btn">
          <a href="#" onClick={this.handleSubmit} className="btn btn-primary btn-md">login <i className="fa fa-sign-in"></i></a>
          </span>
           {errors}
          </div>
        </div>
        </div>
        </div>
        </div>
    );
  }
});
