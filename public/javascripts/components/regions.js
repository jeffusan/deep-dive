/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */

var CreateRegion = React.createClass({
  render: function() {
    return (
            /* jshint ignore:start */
<div className="modal fade">
  <div className="modal-dialog">
    <div className="modal-content">
      <div className="modal-header">
        <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 className="modal-title">Modal title</h4>
      </div>
      <div className="modal-body">
        <p>One fine body&hellip;</p>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" className="btn btn-primary">Save changes</button>
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

        <li className="list-group-item" id={this.props.id}>
        <span className="badge selectable">Delete</span>
        <span className="badge selectable">Edit</span>
        {this.props.name}</li>
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
        <ul className="list-group">
                {regionNodes}
        </ul>
    );
  }
});


var Regions = React.createClass({

  handleClick: function(event) {
    
  },

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
        <h2>Regions <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
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
