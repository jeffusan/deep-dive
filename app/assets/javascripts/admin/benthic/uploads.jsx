define(function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var BenthicUploadForm = require('jsx!admin/benthic/uploadform');
  var Message = require('jsx!admin/common/message');

  var BenthicUpload = React.createClass({

    getInitialState: function() {

      return {
        hasMessage: false,
        message: ''
      };
    },

    onSubmit: function(data) {

      var formData = new FormData();
      formData.append('depth', data.depth);
      formData.append('length', data.length);
      formData.append('photographer', data.photographer);
      formData.append('monitoring', data.monitoring);
      formData.append('analyzer', data.analyzer);
      formData.append('eventDate', data.eventDate);
      formData.append('inputFile', data.inputFile, data.inputFileName);
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
            console.log("SUCCESS: - " + data.message);
            this.setState({hasMessage: true, message: data.message});
          } else {
            // Handle errors here
            console.log('ERRORS: ' + data.error);
            this.setState({hasMessage: true, message: data.error});
          }
        }.bind(this),
        error: function(jqXHR, textStatus, errorThrown) {
          // Handle errors here
          console.log('ERRORS: ' + textStatus);
          this.setState({hasMessage: true, message: data.error});
          // STOP LOADING SPINNER
        }.bind(this)
      });
    },

    render: function() {

      var showMessage = this.state.hasMessage ?
          <Message message={this.state.message}/> :
          <span/>;

      return (
          <div className="container-fluid">
          <div className="panel panel-default">
          <div className="panel-heading clearfix">
          <h3 className="panel-title pull-left">Submit Benthic Data</h3>
          </div>
          <div className="panel-body">
          {showMessage}
          <BenthicUploadForm ref="uploadForm" onBenthicSubmit={this.onSubmit}/>
        </div>
          <div className="panel-footer">
          </div>
          </div>
        </div>
      );
    }

  });
  return BenthicUpload;
});
