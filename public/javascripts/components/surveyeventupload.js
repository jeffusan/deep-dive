/*global React:false */
/*global $:false */

var Grid = ReactBootstrap.Grid;
var Row = ReactBootstrap.Row;
var Col = ReactBootstrap.Col;

var UploadForm = React.createClass({

  getInitialState: function() {
    return {
      value: 'Yodel'
    };
  },

  validationState: function() {
    var length = this.state.value.length;
    return 'success';
  },

  handleChange: function(newValue) {
    this.setState({
      value: this.refs.input.getValue()
    });
    this.props.onHandleChange({value: this.refs.input.getValue()});
  },

  onSubmit: function() {
    console.log("On Submit");
  },

  render: function() {

    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
           <div className="col-lg-9 page-header">
        <h3>Upload a Survey Event</h3>
        <form>
        <Row className="show-grid admin-grid">
        <Col xs={6} md={4} className="bg-info"><div className="admin-input-label">Transect Depth</div></Col>
        <Col xs={6} md={6}>
        <Input
          type="text"
          value={this.state.value}
          placeholder="Enter text"
          label="Working example with validation"
          help="Validates based on string length."
          bsStyle={this.validationState()}
          hasFeedback
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} /></Col>
        <Col xs={6} md={2}><div></div></Col>
                 </Row>
        <Row className="show-grid admin-grid">
        <Col xs={6} md={4} className="bg-info"><div className="admin-input-label">Photographer</div></Col>
        <Col xs={6} md={6}>
        <Input
          type="text"
          value={this.state.value}
          placeholder="Enter text"
          label="Working example with validation"
          help="Validates based on string length."
          bsStyle={this.validationState()}
          hasFeedback
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} /></Col>
        <Col xs={6} md={2}><div></div></Col>
                 </Row>
        <Row className="show-grid admin-grid">
        <Col xs={6} md={4} className="bg-info"><div className="admin-input-label">Analyzer</div></Col>
        <Col xs={6} md={6}>
        <Input
          type="text"
          value={this.state.value}
          placeholder="Enter text"
          label="Working example with validation"
          help="Validates based on string length."
          bsStyle={this.validationState()}
          hasFeedback
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} /></Col>
        <Col xs={6} md={2}><div></div></Col>
        </Row>
        <Row className="show-grid admin-grid">
                <Col xs={6} md={4} className="bg-info"><div className="admin-input-label">Event Date</div></Col>
        <Col xs={6} md={6}>
        <Input
          type="text"
          value={this.state.value}
          placeholder="Enter text"
          label="Working example with validation"
          help="Validates based on string length."
          bsStyle={this.validationState()}
          hasFeedback
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} /></Col>
        <Col xs={6} md={2}><div></div></Col>
        </Row>
        <Row className="show-grid admin-grid">
        <Col xs={6} md={4} className="bg-info"><div className="admin-input-label">Input Data</div></Col>
        <Col xs={6} md={6}>
        <Input
      type="file"
      label="File"
      help="[Optional] Block level help text"          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
 /></Col>
        <Col xs={6} md={2}><div></div></Col>
        </Row>
                <Row className="show-grid admin-grid">
        <Input type="submit" value="Submit button" onClick={this.onSubmit} />
</Row>

        </form>
           </div>
          </div>
        </div>
      </div>
    );
  }

});
