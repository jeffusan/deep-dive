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



var DeleteModal = React.createClass({

  delete: function(event) {
    event.preventDefault();
    var id = this.props.container.props.id;
    this.props.container.props.onDelete({id: id});
    this.props.onRequestHide();
  },

  render: function() {
    return (
      <Modal title='Are You Sure???' animation>
        <div className="modal-body">
        Deleting a region will delete all of that region's data too.
        </div>
        <div className="modal-footer">
          <Button bsStyle="danger" onClick={this.delete}>Danger Zone</Button>
          <Button onClick={this.props.onRequestHide}>Cancel</Button>
        </div>
      </Modal>
    );
  }
});

var DeleteTrigger = React.createClass({

  render: function() {
    return (
      <div >
        <ModalTrigger modal={<DeleteModal container={this} />} container={this}>
          <Badge regionId={this.props.id} id="edit-delete-badge" className="selectable" bsStyle="danger">Delete</Badge>
        </ModalTrigger>
      </div>
    );
  }
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

var Region = React.createClass({
  onRegionDelete: function(value) {
      this.props.container.props.onRegionListDelete({id: value.id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info">
        <h4>{this.props.name}
        <span className="pull-right">
            <DeleteTrigger container={this} id={this.props.id}/>
          </span></h4>
        </ListGroupItem>
      /* jshint ignore:end */
    );
  }
});

var RegionList = React.createClass({
  onRegionListDelete: function(value) {
      this.props.onDelete({id: value.id});
  },

  render: function() {

    var regionNodes = this.props.data.map(function (region) {
      return (
          <Region id={region.id} name={region.name}/>
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

  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false
    };
  },

  handleDelete: function() {
    console.log("ID is: " + this.props.id);
    $.ajax({
      'type': 'DELETE',
      'url': '/regions',
      'contentType': 'application/json',
      'data': JSON.stringify({'id': this.props.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var regions = this.state.data;
          var remainingRegions = someArray
               .filter(function (reg) {
                 return reg.id !== this.props.id;
               });
          this.setState({
            data: remainingRegions,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those regions you wanted canceled...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
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
               <RegionList onDelete={this.handleDelete} data={this.state.data} />
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
