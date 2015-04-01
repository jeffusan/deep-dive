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

    handleSubmit: function(e) {
      e.preventDefault();
      var depth = React.findDOMNode(this.refs.depth).value.trim();
      var length = React.findDOMNode(this.refs.length).value.trim();
      var photo = React.findDOMNode(this.refs.photographer).value.trim();
      var monit = React.findDOMNode(this.refs.monitoring).value.trim();
      var analy = React.findDOMNode(this.refs.analyzer).value.trim();
      var evet = React.findDOMNode(this.refs.eventDate).value.trim();
      var inputFile = React.findDOMNode(this.refs.inputFile).files[0];
      var inputFileName = React.findDOMNode(this.refs.inputFile).value.trim();

      if (!depth || !length) {
        return;
      }
      this.props.onBenthicSubmit({
        depth: depth,
        length: length,
        photographer: photo,
        monitoring: monit,
        analyzer: analy,
        eventDate: evet,
        inputFile: inputFile,
        inputFileName: inputFileName
      });
      React.findDOMNode(this.refs.depth).value = '';
      React.findDOMNode(this.refs.length).value = '';
      React.findDOMNode(this.refs.photographer).value = '';
      React.findDOMNode(this.refs.monitoring).value = '';
      React.findDOMNode(this.refs.analyzer).value = '';
      React.findDOMNode(this.refs.eventDate).value = '';
      React.findDOMNode(this.refs.inputFile).value = '';

      return;
    },

    componentDidMount: function() {
      console.log("here");
      $('#sandbox-container input').datepicker({
        format: 'mm/dd/yyyy'
      });
    },

    getInitialState: function() {
      return {errors: {}};
    },

    render: function() {
      return (
        <form className="benthicUploadForm" onSubmit={this.handleSubmit}>
         <div className="form-horizontal">
          {this.renderTextInput('depth', 'Transect Depth')}
          {this.renderTextInput('length', 'Transect Length')}
          {this.renderTextInput('photographer', 'Photographer')}
          {this.renderTextInput('monitoring', 'Monitoring Teams')}
          {this.renderTextInput('analyzer', 'Analyzer')}
        {this.renderDateInput('eventDate', 'Event Date')}

        {this.renderFileInput('inputFile', 'Input File')}
        <input type="submit" value="Post" />
        </div>
          </form>
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
          <input className="form-control" id={id} ref={id} type="text"/>
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
