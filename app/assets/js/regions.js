/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */
/*global Reactable:false */

var Table = Reactable.Table;
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
              <Table className="table table-bordered table-hover" data={this.state.data} itemsPerPage={4} sortable={true}/>
            </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
