var User = React.createClass({
  mixins: [Authentication],

  render: function() {
    var token = auth.getToken();

    return (
      <div class="dd-well">
        <h1>User page</h1>
        <p>You made it!</p>
        <p>{token}</p>
        <Link to="logout">Log out</Link>
      </div>
    );

  }
});
