/*global React:false*/
/*global $:false*/
/*global ReactBootstrap:false*/

var Input = ReactBootstrap.Input;
var Modal = ReactBootstrap.Modal;
var ButtonToolbar = ReactBootstrap.ButtonToolbar;

var CreateRegionTrigger = React.createClass({

  handleDataSubmit: function(value) {
    this.props.onHandlingData({name: value.name});
  },

  render: function() {
    return (
        <ModalTrigger modal={<CreateRegion onCreateRegionSubmit={this.handleDataSubmit}/>}>
          <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
            <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
          </button>
        </ModalTrigger>
    );
  }
});


var CreateRegionInput = React.createClass({

  getInitialState: function() {
    return {
      value: ''
    };
  },

  validationState: function() {
    var length = this.state.value.length;
    if (length > 10) {
     return 'success';
    } else if (length > 5){
      return 'warning';
    } else if (length > 0) {
     return 'error';
    }
    return null;
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
      label="Name of the Region"
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

var CreateRegion = React.createClass({

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
    this.props.onCreateRegionSubmit({name: name});
    this.props.onRequestHide();
  },

  render: function() {
    return (
        <Modal title="Add A Region" animation={true}>
          <div className="modal-body">
            <CreateRegionInput onHandleChange={this.updateName} />
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
