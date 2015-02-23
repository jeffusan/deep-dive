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
var ButtonToolbar = ReactBootstrap.ButtonToolbar;

var CreateSubRegionInput = React.createClass({

  getInitialState: function() {
    return {
      value: ''
    };
  },

  validationState: function() {
    var length = this.state.value.length;
    if (length > 10) return 'success';
    else if (length > 5) return 'warning';
    else if (length > 0) return 'error';
  },

  handleChange: function(newValue) {
    // This could also be done using ReactLink:
    // http://facebook.github.io/react/docs/two-way-binding-helpers.html
    this.setState({
      value: this.refs.input.getValue()
    });
    this.props.onHandleChange({name: this.refs.input.getValue()});
  },

  render: function() {
    return (
        <Input
      type="text"
      value={this.state.value}
      placeholder="Enter name"
      label="Name of the SubRegion"
      help="Validates based on string length."
      bsStyle={this.validationState()}
      hasFeedback
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange} />
    );
  }
});

var CreateSubRegion = React.createClass({

  getInitialState: function() {
    return {
      value: ''
    };
  },

  updateName: function(nameValue) {
    this.state.value = nameValue;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var name = this.state.value.name;
    this.props.onCreateSubRegionSubmit({name: name});
    this.props.onRequestHide();
  },

  render: function() {
    return (
        <Modal title="Add A SubRegion" animation={true}>
          <div className="modal-body">
            <CreateSubRegionInput onHandleChange={this.updateName} />
             <div className="modal-footer">
               <ButtonToolbar>
                 <Button onClick={this.handleSubmit} bsStyle="primary" bsSize="large">Create</Button>
                 <Button onClick={this.props.onRequestHide}>Close</Button>
               </ButtonToolbar>
             </div>
           </div>
        </Modal>
    );
  }
});

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
        Deleting a subregion will delete all of that region's data too.
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
      /* jshint ignore:start */
      <div >
        <ModalTrigger modal={<DeleteModal container={this} />} container={this}>
          <Badge subRegionId={this.props.id} id="edit-delete-badge" className="selectable" bsStyle="danger">Delete</Badge>
        </ModalTrigger>
      </div>
      /* jshint ignore:end */
    );
  }

});

var CreateSubRegionTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({name: value.name});
  },

  render: function() {
    return (
        <ModalTrigger modal={<CreateSubRegion onCreateSubRegionSubmit={this.handleDataSubmit}/>}>
          <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
            <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
          </button>
        </ModalTrigger>
    );
  }
});

var SubRegion = React.createClass({

  componentDidMount: function() {
      $('#' + this.props.id).editable({
      type: 'text',
      pk: this.props.id,
      url: '/subregions',
      ajaxOptions: {
        headers: {
          'X-XSRF-TOKEN': auth.getToken()
        },
        'dataType': 'json',
        'contentType': 'application/json'
      },
      params: function(params) { return JSON.stringify(params); }
    });
  },

  onSubRegionDelete: function(value) {
      this.props.onSubRegionDelete({id: value.id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info">
        <h4><a href="#" id={this.props.id} ref="input">{this.props.name}</a>
        <span className="pull-right">
            <DeleteTrigger onDelete={this.onSubRegionDelete} id={this.props.id}/>
          </span></h4>
        </ListGroupItem>
    );
  }
});


var SubRegionList = React.createClass({

  onDelete: function(value) {
      this.props.onDelete({id: value.id});
    },

  render: function() {

    var subRegionNodes = this.props.data.map(function (subRegion) {
      return (
          <SubRegion onSubRegionDelete={this.onDelete} id={subRegion.id} name={subRegion.name}/>
      );
    }.bind(this));

    return (
      <ListGroup>
                {subRegionNodes}
      </ListGroup>
    );
  }
});


var SubRegions = React.createClass({

  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false
    };
  },

  handleDelete: function(value) {
    $.ajax({
      'type': 'DELETE',
      'url': '/subregions/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var regions = this.state.data;
          var remainingSubRegions = regions
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainingSubRegions,
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
          var newSubRegions = regions.concat([data]);
          this.setState({
            data: newSubRegions,
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
      'url': '/subregions',
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
               <h2>Sub Regions         <CreateSubRegionTrigger onHandlingData={this.handleCreate}/></h2>
               <hr/>
               {maybeMessage}
               <SubRegionList onDelete={this.handleDelete} data={this.state.data} />
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }

});
