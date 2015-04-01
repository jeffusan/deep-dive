define(function(require) {
  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var ListGroup = ReactBootstrap.ListGroup;
  var ListGroupItem = ReactBootstrap.ListGroupItem;
  var DeleteItem = require('jsx!admin/common/delete');
  var auth = require('auth/auth');
  var Pager = require('admin/common/pager.min');

  var SurveyEvent = React.createClass({

    itemDelete: function(event) {
      var id = this.props.key;
      this.props.onSurveyEventDelete({id: id});
    },

    render: function() {
      return (
        <ListGroupItem bsStyle="info">
         <h4>{this.props.name}</h4> ({this.props.eventDate})
          <span className="pull-right">
           <DeleteItem
             title="Delete this SurveyEvent?"
             message="Deleting this survey event will also delete the associated data!"
             delete={this.itemDelete} />
          </span>
        </ListGroupItem>
      );
    }
  });

  var SurveyEventList = React.createClass({

    getInitialState: function() {
      return {
        total: 0,
        current: 0,
        visiblePage: 10
      }
    },

    handlePageChanged: function ( newPage ) {
      this.setState({ current : newPage });
    },

    itemDelete: function(event) {
      var id = this.props.key;
      this.props.delete(event);
    },

    render: function() {

      var surveyevents = function() {
        var start = this.state.current * this.state.visiblePage;
        var end = 0;
        if((this.props.data.length - start) >= 10) {
          end = start + 10;
        } else {
          end = this.props.data.length;
        }
        var s = [];
        var sliceOHeaven = this.props.data.slice(start, end);
        for (i = 0; i < sliceOHeaven.length; i++) {
          var reg = sliceOHeaven[i];
          s.push(
            <SurveyEvent
              onSurveyEventDelete={this.itemDelete}
              key={reg.id}
              eventDate={reg.event_date}/>
          );
        }
        return s;

      }.bind(this);

      return (
        <div>
          <Pager total={this.props.data.length / 10}
            current={this.state.current}
            visiblePages={this.state.visiblePage}
            onPageChanged={this.handlePageChanged}/>
          <ListGroup>
            {surveyevents()}
          </ListGroup>
        </div>
      );
    }
  });

  var SurveyEvents = React.createClass({

    getInitialState: function() {
      return {
        data: []
      };
    },

    delete: function(value) {
      $.ajax({
        'type': 'DELETE',
        'url': '/surveyevents/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var surveyEvents = this.state.data;
          var remainder = surveyEvents
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainder,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {}
          });
        }
      }.bind(this)
    });

  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/surveyevents',
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

    var maybeMessage = this.state.hasMessage ?
      <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
      <span />;

    return (

      <div className="container-fluid">
        <div className="panel panel-default">
          <div className="panel-heading clearfix">
            <h3 className="panel-title pull-left">Survey Events</h3>
          </div>
          <div className="panel-body">
            <SurveyEventList delete={this.delete} data={this.state.data} />
          </div>
          <div className="panel-footer">
            {maybeMessage}
          </div>
        </div>
      </div>
    );
    }

  });

  return SurveyEvents;
});