/*global React:false */
/*global $:false */

var CreateTransectDepthInput = React.createClass({

  getInitialState: function() {
    return {
      depth: ''
    };
  },

  handleChange: function(newValue) {
    this.setState({
      depth: this.refs.input.getValue()
    });
    this.props.onHandleChange({depth: this.refs.input.getValue()});
  },

  render: function() {
    return (
        <Input
      type="text"
      value={this.state.depth}
      placeholder="Enter transect depth"
      label="Transect Depth"
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange}/>
    );
  }
});

var CreateAnalyzerInput = React.createClass({

  getInitialState: function()  {
    return {
      analyzer: ''
    };
  },

  handleChange: function(newValue) {
    this.setState({
      analyzer: this.refs.input.getValue()
    });
    this.props.onHandleChange({analyzer: this.refs.input.getValue()});
  },

  render: function() {
    return(
        <Input
      type="text"
      value={this.state.analyzer}
      placeholder="Analyzer Initials"
      label="Analyzer"
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange}/>
    );
  }
});

var CreateEventDateInput = React.createClass({

  getInitialState: function() {
    return {
      eventDate: ''
    };
  },

  handleChange: function(newValue) {
    this.setState({
      eventDate: this.refs.input.getValue()
    });
    this.props.onHandleChange({eventDate: this.refs.input.getValue()});
  },

  render: function() {
    return(
        <Input
      type="text"
      value={this.state.eventDate}
      placeholder="Date of Survey Event"
      label="Event Date"
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange}/>
    );
  }
});

var CreatePhotographerInput = React.createClass({

  getInitialState: function() {
    return {
      photographer: ''
    };
  },

  handleChange: function(newValue) {
    this.setState({
      photographer: this.refs.input.getValue()
    });
    this.props.onHandleChange({photographer: this.refs.input.getValue()});
  },

  render: function() {
    return(
        <Input
      type="text"
      value={this.state.photographer}
      placeholder="Photographer Initials"
      label="Photographer"
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange}/>
    );
  }
});

var Upload = React.createClass({

  getInitialState: function() {
    return {
      depth: '',
      photographer: '',
      analyzer: '',
      event_date: ''
    };
  },

  updateEventDate: function(eventDateValue) {
    this.state.eventDate = eventDateValue;
  },

  updateDepth: function(depthValue) {
    this.state.depth = depthValue;
  },

  updatePhotgrapher: function(photographerValue) {
    this.state.photographer = photographerValue;
  },

  updateAnalyzer: function(analyzerValue) {
    this.state.analyzer = analyzerValue;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var transect_depth = this.state.depth.depth;
    var photographer = this.state.photographer.photographer;
    var analyzer = this.analyzer.analyzer;
    var event_date = this.event_date.event_date;
    this.props.onCreateBenthicInput({
      transect_depth: transect_depth,
      photographer: photographer,
      analyzer: analyzer,
      event_date: event_date
    });
  },

  render: function() {
    return (
      <div className="modal-body">
        <CreateTransectDepthInput onHandleChange={this.updateDepth}/>
        <CreatePhotographerInput onHandleChange={this.updatePhotographer}/>
        <CreateAnalyzerInput onHandleChange={this.updateAnalyzer}/>
        <CreateEventDateInput onHandleChange={this.updateEventDate}/>
        <input type="file" accept=".xlsx" name="inputData" />
        <Button onClick={this.handleSubmit} bsStyle="primary" bsSize="large">Upload</Button>
      </div>
    );
  }
});

var UploadForm = React.createClass({

  render: function() {

    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
           <div className="col-lg-9 page-header">
            <h3>Upload a Survey Event</h3>
               <Upload/>
             </div>
          </div>
        </div>
      </div>
    );
  }

});
