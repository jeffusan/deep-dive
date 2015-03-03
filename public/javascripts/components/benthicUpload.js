/*global React:false */
/*global $:false */

var Grid = ReactBootstrap.Grid;
var Row = ReactBootstrap.Row;
var Col = ReactBootstrap.Col;


var BenthicUpload = React.createClass({

  getInitialState: function() {
    return {
      value: 'Yodel',
      question: '',
      email: ''
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
    if (this.refs.uploadForm.isValid()) {
      this.setState({submitted: this.refs.uploadForm.getFormData()});
    }
  },

  render: function() {

    var submitted = function() {
    if (this.state.submitted !== null) {
      return <div className="alert alert-success">
        <p>ContactForm data:</p>
        <pre><code>{JSON.stringify(this.state.submitted, null, '  ')}</code></pre>
        </div>;
    } else {
      return <div></div>;
    }
    };

    return (
      <div className="container-fluid">
      <div className="panel panel-default">
        <div className="panel-heading clearfix">
          <h3 className="panel-title pull-left">Submit Benthic Data</h3>
        </div>
        <div className="panel-body">
        <BenthicUploadForm ref="uploadForm"/>
        </div>
        <div className="panel-footer">
          <button type="button" className="btn btn-primary btn-block" onClick={this.onSubmit}>Submit</button>
        </div>
      </div>
      {submitted}
    </div>
    );
  }

});
