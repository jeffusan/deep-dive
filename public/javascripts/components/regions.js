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

var Region = React.createClass({
  render: function() {
    return (
      /* jshint ignore:start */
      <div>
      <div className="col-xs-8 col-sm-6" id={this.props.id}>
        {this.props.name}
      </div>
      <div className="col-xs-4 col-sm-6">
        <span className="glyphicon glyphicon-pencil" aria-hidden="true"></span>
        <span className="glyphicon glyphicon-minus" aria-hidden="true"></span>
      </div>
      </div>
      /* jshint ignore:end */
    );
  }
});


var RegionList = React.createClass({

  render: function() {
    var regionNodes = this.props.data.map(function (region) {
      return (
        /* jshint ignore:start */
          <Region id={region.id} name={region.name}/>
        /* jshint ignore:end */
      );
    });
    return (
        <div className="row bg-success">
        {regionNodes}
        </div>
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
            <h3 id='errors'>{this.state.mess}</h3>
             <div className="col-lg-9 page-header">
               <h2>Regions <button type="button" className="btn btn-default" aria-label="Left Align">
                <span className="glyphicon glyphicon-plus" aria-hidden="true"></span></button></h2>
        <hr/>
        <RegionList data={this.state.data} />

             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }
});
