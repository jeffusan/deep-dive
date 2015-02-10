var routes = (
  <Route handler={App}>
    <Route name="login" handler={Login}/>
    <Route name="logout" handler={Logout}/>
    <Route name="dashboard" path="dashboard/?:selection?" handler={Dashboard}/>
    <Route name="user" path="user/:selection" handler={User}/>
  </Route>
);

Router.run(routes, function (Handler, state) {
  React.render(<Handler/>, document.getElementById('magic'));
});
