/*global React:false*/
/*global $:false*/
/*global ReactBootstrap:false*/

var Input = ReactBootstrap.Input;
var Modal = ReactBootstrap.Modal;
var ButtonToolbar = ReactBootstrap.ButtonToolbar;

var CreateSiteTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({
      name: value.name,
      latitude: value.latitude,
      longitude: value.longitude,
      map_datum: value.map_datum,
      subregion_id: value.subregion_id
    });
  },

  render: function() {
    return (
        <ModalTrigger modal={<CreateSite onCreateSite={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
          <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        </ModalTrigger>
    );
  }
});

var CreateSiteNameInput = React.createClass({

  getInitialState: function() {
    return {
      name: ''
    };
  },

  validationState: function() {
    var length = this.state.name.length;
    return 'success';
  },

  handleChange: function(newValue) {
    this.setState({
      name: this.refs.input.getValue()
    });
    this.props.onHandleChange({name: this.refs.input.getValue()});
  },

  render: function() {
    return (
        <Input
      type="text"
      value={this.state.name}
      placeholder="Enter name"
      label="Name of the Site"
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

var CreateSiteLatitudeInput = React.createClass({

  getInitialState: function() {
    return {
      latitude: ''
    };
  },

  validationState: function() {
    var length = this.state.latitude.length;
    return 'success';
  },

  handleChange: function(newValue) {
    this.setState({
      latitude: this.refs.input.getValue()
    });
    this.props.onHandleChange({name: this.refs.input.getValue()});
  },

  render: function() {
    return (
        <Input
      type="text"
      value={this.state.latitude}
      placeholder="Enter latitude"
      label="Latitude of the Site"
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

var CreateSiteLongitudeInput = React.createClass({

  getInitialState: function() {
    return {
      longitude: ''
    };
  },

  validationState: function() {
    var length = this.state.longitude.length;
    return 'success';
  },

  handleChange: function(newValue) {
    this.setState({
      longitude: this.refs.input.getValue()
    });
    this.props.onHandleChange({code: this.refs.input.getValue()});
  },

    render: function() {
    return (
        <Input
      type="text"
      value={this.state.longitude}
      placeholder="Enter longitude"
      label="Longitude of the Site"
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

var CreateSite = React.createClass({

  getInitialState: function() {
    return {
      name: '',
      latitude: '',
      longitude: '',
      map_datum: ''
    };
  },

  updateName: function(nameValue) {
    this.state.name = nameValue;
  },

  updateLatitude: function(latitudeValue) {
    this.state.latitude = latitudeValue;
  },

  updateLongitude: function(latitudeValue) {
    this.state.longitude = longitudeValue;
  },

  updateMapDatum: function(mapDatum) {
    this.state.mapDatum = mapDatum;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var name = this.state.name.name;
    var latitude = this.state.latitude.latitude;
    var longitude = this.state.longitude.longitude;
    var mapDatum = this.state.mapDatum.mapDatum;
    this.props.onCreateSubRegion({
      name: name,
      latitude: latitude,
      longitude: longitude,
      map_datum: mapDatum
    });
    this.props.onRequestHide();
  },

  render: function() {
    return (
        <Modal title="Add a Reef Type" animation={true}>
        <div className="modal-body">
        <CreateSiteNameInput onHandleChange={this.updateName}  />
        <CreateSiteLatitudeInput onHandleChange={this.updateLatitude} />
        <CreateSiteLongitudeInput onHandleChange={this.updateLongitude} />
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
