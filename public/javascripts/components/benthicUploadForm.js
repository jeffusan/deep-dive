/*global React:false */
/*global $:false */

var Grid = ReactBootstrap.Grid;
var Row = ReactBootstrap.Row;
var Col = ReactBootstrap.Col;

var trim = function() {
  var TRIM_RE = /^\s+|\s+$/g;
  return function trim(string) {
    return string.replace(TRIM_RE, '');
  };
}();

function $c(staticClassName, conditionalClassNames) {
  var classNames = [];
  if (typeof conditionalClassNames == 'undefined') {
    conditionalClassNames = staticClassName;
  }
  else {
    classNames.push(staticClassName);
  }
  for (var className in conditionalClassNames) {
    if (!!conditionalClassNames[className]) {
      classNames.push(className);
    }
  }
  return classNames.join(' ');
}

var BenthicUploadForm = React.createClass({

  componentDidMount: function() {
    $('.datepicker').datepicker()
  },

  getInitialState: function() {
    return {errors: {}};
  },

  isValid: function() {
    var fields = ['depth', 'photographer', 'analyzer', 'eventDate', 'inputFile'];
    if (this.props.depth) fields.push('depth');
    if (this.props.photographer) fields.push('photographer');
    if (this.props.analyzer) fields.push('analyzer');
    if (this.props.eventDate) fields.push('eventDate');

    var errors = {};

    fields.forEach(function(field) {
      var value = trim(this.refs[field].getDOMNode().value);
      if (!value) {
        errors[field] = 'This field is required';
      }
    }.bind(this));

    this.setState({errors: errors});

    var isValid = true;

    for (var error in errors) {
      isValid = false;
      break;
    }

    return isValid;
  },

  getFormData: function() {
    var data = {
      depth: this.refs.depth.getDOMNode().value,
      photographer: this.refs.photographer.getDOMNode().value,
      analyzer: this.refs.analyzer.getDOMNode().value,
      eventDate: this.refs.eventDate.getDOMNode().value,
      inputFile: this.refs.inputFile.getDOMNode().files[0],
      inputFileName: this.refs.inputFile.getDOMNode().value
    };
    return data;
  },

  render: function() {
    return (
      <div className="form-horizontal">
      {this.renderTextInput('depth', 'Transect Depth')}
      {this.renderTextInput('photographer', 'Photographer')}
      {this.renderTextInput('analyzer', 'Analyzer')}
      {this.renderTextInput('eventDate', 'Event Date')}
      {this.renderFileInput('inputFile', 'Input File')}
      </div>
    );
  },

  renderField: function(id, label, field) {
    return (
        <div className={$c('form-group', {'has-error': id in this.state.errors})}>
          <label htmlFor={id} className="col-sm-4 control-label">{label}</label>
          <div className="col-sm-6">
            {field}
          </div>
        </div>
    );
  },

  renderDateInput: function(id, label) {
    return this.renderField(
      id,
      label,
      <div className="input-append date">
        <input className="datepicker" type="text" value="12-02-2012" id={id} ref={id} data-provide="datepicker"/>
        <span className="add-on"><i className="icon-th"></i></span>
      </div>
    );
  },

  renderTextInput: function(id, label) {
    return this.renderField(
      id,
      label,
      <input type="text" className="form-control" id={id} ref={id} />
    );
  },

  renderFileInput: function(id, label) {
    return this.renderField(
      id,
      label,
      <input type="file" id={id} ref={id} accept=".xlsx"/>
    );
  }
});
