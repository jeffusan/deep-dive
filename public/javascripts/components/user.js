/*jshint strict:false */
/*global React:false */
/*global Authentication:false */
/*global auth:false */
/*global $:false */
var User = React.createClass({
  mixins: [Authentication],

  render: function() {
    var token = auth.getToken();
    console.log("remove");
    $( "#wrapper" ).remove();
    return (
      /* jshint ignore:start */
      <div className="dd-well">
        <h1>User page</h1>
        <p>You made it!</p>
        <p>{token}</p>
        <Link to="logout">Log out</Link>
        </div>
        /* jshint ignore:end */
    );

  }
});
