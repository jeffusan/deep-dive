define(function(require) {
  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');

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
    } else {
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
      console.log("here");
      $('#sandbox-container input').datepicker({
        format: 'mm/dd/yyyy'
      });
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
          {this.renderTextInput('height', 'Transect Height')}
          {this.renderTextInput('photographer', 'Photographer')}
          {this.renderTextInput('monitoringteams', 'Monitoring Teams')}
          {this.renderTextInput('analyzer', 'Analyzer')}
          {this.renderDateInput('eventDate', 'Event Date')}
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
        <div className="row form-horizontal">
          <div id="sandbox-container" className="span5 col-md-5">
          <input className="form-control" type="text"/>
          </div>
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
  return BenthicUploadForm;
});
