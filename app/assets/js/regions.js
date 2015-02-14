/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */
var Regions = React.createClass({

  getInitialState: function() {
    return {
      something: {},
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
            something: data,
            message: 'Roger that'
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            something: {},
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
