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

    delete: function(value) {
      console.log("value: " + value.id);
      $.ajax({
        'type': 'DELETE',
        'url': '/sites/' + value.id,
        'contentType': 'application/json',
        'data': JSON.stringify({'id': value.id}),
        'dataType': 'json',
        'async': false,
        'headers': {
          'X-XSRF-TOKEN': auth.getToken()
        },
        'success' : function(data) {
          console.log("GREAT");
          if(this.isMounted()) {
            var sites = this.state.data;
            var remainingSites = sites.filter(function (reg) {
              return reg.id !== value.id;
            });
            this.setState({
              data: remainingSites,
              message: 'Gone!',
              hasMessage: true
            });
          }
        }.bind(this),
        'error': function(data) {
          console.log("ERR");
          if(this.isMounted()) {
            this.setState({
              data: {},
              message: 'Um, yeah. About those sites you wanted canceled...',
              hasMessage: true
            });
          }
        }.bind(this)
      });
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
          <SiteList data={this.state.data} delete={this.delete} />
        </AdminPanel>
      );
    }

  });

  return Sites;
});
