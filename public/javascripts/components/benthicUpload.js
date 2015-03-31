/*global React:false */
/*global $:false */

var Grid = ReactBootstrap.Grid;
var Row = ReactBootstrap.Row;
var Col = ReactBootstrap.Col;

  var BenthicUpload = React.createClass({

    getInitialState: function() {
      return {
        submitted: {},
        loaded: false,
        message: ''
      };
    },

    onSubmit: function() {

      if (this.refs.uploadForm.isValid()) {
        this.setState({
          submitted: this.refs.uploadForm.getFormData(),
          loaded: false,
          message: ''
        });
      }

      var formData = new FormData();
      formData.append('depth', this.state.submitted.depth);
      formData.append('photographer', this.state.submitted.photographer);
      formData.append('analyzer', this.state.submitted.analyzer);
      formData.append('eventDate', this.state.submitted.eventDate);
      formData.append('inputFile', this.state.submitted.inputFile, this.state.submitted.inputFileName);

      $.ajax({
        type: 'POST',
        url: '/benthic',
        processData: false,
        contentType: false,
        dataType: 'json',
        data: formData,
        success: function(data, textStatus, jqXHR) {
          if(typeof data.error === 'undefined') {
          // Success so call function to process the form
          //submitForm(event, data);
          console.log("SUCCESS");
          this.setState({submitted: {}, loaded: true, message: data.message});
          } else {
          // Handle errors here
          console.log('ERRORS: ' + data.error);
          this.setState({submitted: {}, loaded: true, message: data.error});
          }
        }.bind(this),
        error: function(jqXHR, textStatus, errorThrown) {
        // Handle errors here
        console.log('ERRORS: ' + textStatus);
        this.setState({submitted: formData, loaded: true, message: data.error});
        // STOP LOADING SPINNER
        }.bind(this)
      });
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
        <Message message={this.state.message}/>

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
