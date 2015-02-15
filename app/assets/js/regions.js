/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */

var CreateRegion = React.createClass({
  render: function() {
    return (
            /* jshint ignore:start */
<div class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Modal title</h4>
      </div>
      <div class="modal-body">
        <p>One fine body&hellip;</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
        </div>
              /* jshint ignore:end */
    );
  }
});

var Regions = React.createClass({


  getInitialState: function() {
    return {
      data: [],
      mess: ''
    };
  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/regions',
      'contentType': 'application/json',
      'async': 'false',
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          this.setState({
            data: data,
            message: 'Roger that'
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            mess: 'Big Error'
          });
        }
      }.bind(this)
    });
    console.log("finished call!");
  },

  render: function() {

    return (
      /* jshint ignore:start */
        <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-12">
              <h1 className="page-header">
              Regions
              </h1>
              <h3 id='errors'>{this.state.mess}</h3>
            </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
