/*jshint strict:false */
/*global React:false*/

var SurveyEvent = React.createClass({

  itemDelete: function(event) {
    var id = this.props.id;
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

  itemDelete: function(event) {
    var id = this.props.id;
    this.props.delete(event);
  },

  render: function() {

    var surveyEvents = this.props.data.map(function(surveyEvent) {
      return (
        <SurveyEvent onSurveyEventDelete={this.itemDelete} id={surveyEvent.id} eventDate={surveyEvent.event_date}/>
      );
    }.bind(this));

    return (
        <ListGroup>
        {surveyEvents}
        </ListGroup>
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

  create: function(value) {

    $.ajax({
      'type': 'PUT',
      'url': '/surveyevents',
      'contentType': 'application/json',
      'data': JSON.stringify({
        'eventDate': value.name
      }),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var surveyevents = this.state.data;
          var results = surveyevents.concat([data]);
          this.setState({
            data: results,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those reeftypes...',
            hasMessage: true
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

    return (
      /* jshint ignore:start */
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
             <div className="col-lg-9 page-header">
               <h2>Survey Events </h2>
               <hr/>
               <SurveyEventList delete={this.delete} data={this.state.data}/>
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }

});
