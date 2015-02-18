/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */
/*global ReactBootstrap:false*/

var ListGroup = ReactBootstrap.ListGroup;
var ListGroupItem = ReactBootstrap.ListGroupItem;
var Badge = ReactBootstrap.Label;
var Modal = ReactBootstrap.Modal;
var ModalTrigger = ReactBootstrap.ModalTrigger;
var Button = ReactBootstrap.Button;
var Input = ReactBootstrap.Input;

var EditRegion = React.createClass({
  /* jshint ignore:start */
  render: function() {
    return (
        <Modal title="Edit A Region" animation={true}>
          <div className="modal-body">
        <h4>Add the form here...</h4>
        <p>Duis mollis, est non commodo luctus, nisi erat porttitor ligula.</p>


          <div className="modal-footer">
            <Button onClick={this.props.onRequestHide}>Close</Button>
          </div>
        </div>
        </Modal>
        );
      }
      /* jshint ignore:end */
});


var CreateRegionTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({name: value.name});
  },

  render: function() {
    return (
      /* jshint ignore:start */
        <ModalTrigger modal={<CreateRegion onCreateRegionSubmit={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
                <span className="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
        </ModalTrigger>
      /* jshint ignore:end */
    );
  }
});

var EditRegionTrigger = React.createClass({
  render: function() {
    return (
      /* jshint ignore:start */
        <ModalTrigger modal={<EditRegion />}>
        <Badge id="edit-delete-badge" className="selectable" bsStyle="primary">Edit</Badge>
        </ModalTrigger>
      /* jshint ignore:end */
    );
  }
});

var Region = React.createClass({
  render: function() {
    return (
      /* jshint ignore:start */
        <ListGroupItem id={this.props.id} bsStyle="info"><h4>{this.props.name}
          <span className="pull-right">
            <EditRegionTrigger/>
            <Badge id="edit-delete-badge" className="selectable" bsStyle="danger">Delete</Badge>
          </span></h4>
        </ListGroupItem>
      /* jshint ignore:end */
    );
  }
});


var RegionList = React.createClass({

  render: function() {
    var regionNodes = this.props.data.map(function (region) {
      return (
        /* jshint ignore:start */
          <Region id={region.id} name={region.name}/>
        /* jshint ignore:end */
      );
    });
    return (
      <ListGroup>
                {regionNodes}
      </ListGroup>
    );
  }
});


var Regions = React.createClass({

  handleClick: function(event) {

  },

  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false
    };
  },

  handleCreate: function(value) {
    console.log("create name: " + value.name);

    $.ajax({
      'type': 'PUT',
      'url': '/regions',
      'contentType': 'application/json',
      'data': JSON.stringify({'name': value.name}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var regions = this.state.data;
          var newRegions = regions.concat([data]);
          this.setState({
            data: newRegions,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those regions...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/regions',
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
    console.log("finished call!");
  },

  render: function() {

    var maybeMessage = this.state.hasMessage ?
          <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
          <span />;

    return (
      /* jshint ignore:start */
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <h3 id='errors'>{this.state.mess}</h3>
             <div className="col-lg-9 page-header">
               <h2>Regions         <CreateRegionTrigger onHandlingData={this.handleCreate}/></h2>
               <hr/>
               {maybeMessage}
               <RegionList data={this.state.data} />
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
