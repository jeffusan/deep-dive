/*global React:false*/
/*global $:false*/
/*global ReactBootstrap:false*/

var Input = ReactBootstrap.Input;
var Modal = ReactBootstrap.Modal;
var ButtonToolbar = ReactBootstrap.ButtonToolbar;

var CreateSubRegionTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({
      name: value.name,
      code: value.code,
      regionId: value.regionId
    });
  },

  render: function() {
    return (
        <ModalTrigger modal={<CreateSubRegion onCreateSubRegion={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
          <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        </ModalTrigger>
    );
  }
});

var CreateSubRegionNameInput = React.createClass({

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

var CreateSubRegionCodeInput = React.createClass({

  getInitialState: function() {
    return {
      code: ''
    };
  },

  validationState: function() {
    var length = this.state.code.length;
    return 'success';
  },

  handleChange: function(newValue) {
    this.setState({
      code: this.refs.input.getValue()
    });
    this.props.onHandleChange({code: this.refs.input.getValue()});
  },

    render: function() {
    return (
        <Input
      type="text"
      value={this.state.code}
      placeholder="Enter code"
      label="Code of the SubRegion"
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

var CreateRegionSelector = React.createClass({

  getInitialState: function() {
    return {
      regionId: '',
      regions: []
    };
  },

  handleChange: function(event) {
    this.setState({
      regionId: this.refs.input.getValue()
    });
    this.props.onHandleChange({regionId: this.refs.input.getValue()});
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
            regions: data
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            regions: []
          });
        }
      }.bind(this)
    });

  },

  render: function() {
    var regionator = this.state.regions.map(function(region) {
      return (
          <option value={region.id}>{region.name}</option>
      );
    }.bind(this));

    return (
      <Input type="select" label='Select' defaultValue="select" ref="input" onChange={this.handleChange}>
        <option value="0">select</option>
        {regionator}
      </Input>
    );
  }
});

var CreateSubRegion = React.createClass({

  getInitialState: function() {
    return {
      name: '',
      code: '',
      regionId: ''
    };
  },

  updateName: function(nameValue) {
    this.state.name = nameValue;
  },

  updateCode: function(codeValue) {
    this.state.code = codeValue;
  },

  updateRegion: function(regionId) {
    this.state.regionId = regionId;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var name = this.state.name.name;
    var code = this.state.code.code;
    var regionId = this.state.regionId.regionId;
    this.props.onCreateSubRegion({
      name: name,
      code: code,
      regionId: regionId
    });
    this.props.onRequestHide();
  },

  render: function() {
    return (
        <Modal title="Add a Sub Region" animation={true}>
        <div className="modal-body">
        <CreateSubRegionNameInput onHandleChange={this.updateName}  />
        <CreateSubRegionCodeInput onHandleChange={this.updateCode} />
        <CreateRegionSelector onHandleChange={this.updateRegion} />
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
