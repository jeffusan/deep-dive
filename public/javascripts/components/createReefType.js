/*global React:false*/
/*global $:false*/
/*global ReactBootstrap:false*/

var Input = ReactBootstrap.Input;
var Modal = ReactBootstrap.Modal;
var ButtonToolbar = ReactBootstrap.ButtonToolbar;

var CreateReefTypeTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({
      name: value.name,
      depth: value.depth
    });
  },

  render: function() {
    return (
        <ModalTrigger modal={<CreateReefType onCreateReefType={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
          <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        </ModalTrigger>
    );
  }
});

var CreateReefTypeNameInput = React.createClass({

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
      label="Name of the Reef Type"
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

var CreateReefTypeDepthInput = React.createClass({

  getInitialState: function() {
    return {
      depth: ''
    };
  },

  validationState: function() {
    var length = this.state.depth.length;
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
      placeholder="Enter depth"
      label="Depth of the Reef Type"
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

var CreateReefType = React.createClass({

  getInitialState: function() {
    return {
      name: '',
      depth: ''
    };
  },

  updateName: function(nameValue) {
    this.state.name = nameValue;
  },

  updateDepth: function(depthValue) {
    this.state.depth = depthValue;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var name = this.state.name.name;
    var code = this.state.depth.depth;
    this.props.onCreateSubRegion({
      name: name,
      depth: depth
    });
    this.props.onRequestHide();
  },

  render: function() {
    return (
        <Modal title="Add a Reef Type" animation={true}>
        <div className="modal-body">
        <CreateReefTypeNameInput onHandleChange={this.updateName}  />
        <CreateReefTypeDepthInput onHandleChange={this.updateDepth} />
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
