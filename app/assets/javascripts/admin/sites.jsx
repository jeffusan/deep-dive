define(function(require){

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var AdminPanel = require('jsx!admin/common/adminpanel');
  var AddSite = require('jsx!admin/addsite');
  var SiteList = require('jsx!admin/sitelist');
  var auth = require('auth/auth');

  var Sites = React.createClass({

    getInitialState: function() {
      return {
        data: [],
        message: '',
        hasMessage: false
      };
    },

    componentDidMount: function() {

      $.ajax({
        'type': 'GET',
        'url': '/sites',
        'contentType': 'application/json',
        'async': 'false',
        'headers': {
          'X-XSRF-TOKEN': auth.getToken()
        },
        'success' : function(data) {
          if(this.isMounted()) {
            this.setState({
              data: data,
              message: '',
              hasMessage: false
            });
          }
        }.bind(this),
        'error': function(data) {
          if(this.isMounted()) {
            this.setState({
              data: {},
              message: 'Big Error',
              hasMessage: true
            });
          }
        }.bind(this)
      });
    },

    render: function() {
      return (
        <AdminPanel name="Sites">
          <AddSite/>
          <SiteList data={this.state.data} />
        </AdminPanel>
      );
    }

  });

  return Sites;
});
