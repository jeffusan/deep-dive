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

var EditRegion = React.createClass({
  render: function() {
    return (
        <Modal {...this.props} title="Edit A Region" animation={true}>
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
});

var CreateRegion = React.createClass({
    render: function() {
    return (
        <Modal {...this.props} title="Add A Region" animation={true}>
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
});

var CreateRegionTrigger = React.createClass({
  render: function() {
    return (
      /* jshint ignore:start */
        <ModalTrigger modal={<CreateRegion />}>
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
      mess: ''
    };
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
            message: 'Roger that'
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            mess: 'Big Error'
          });
        }
      }.bind(this)
    });
    console.log("finished call!");
  },

  render: function() {

    return (
      /* jshint ignore:start */
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <h3 id='errors'>{this.state.mess}</h3>
             <div className="col-lg-9 page-header">
        <h2>Regions         <CreateRegionTrigger/></h2>
        <hr/>
        <RegionList data={this.state.data} />

             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
