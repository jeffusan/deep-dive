/*jshint strict:false */
/*global React:false*/

var ReefType = React.createClass({

  itemDelete: function(event) {
    var id = this.props.id;
    this.props.onReefTypeDelete({id: id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info">
         <h4>{this.props.name}</h4> ({this.props.depth})
          <span className="pull-right">
           <DeleteItem
             title="Delete this ReefType?"
             message="Deleting this reeftype may be constained by it's associations with Sites."
             delete={this.itemDelete} />
          </span>
        </ListGroupItem>
    );
  }
});

var ReefTypeList = React.createClass({

  itemDelete: function(event) {
    var id = this.props.id;
    this.props.delete(event);
  },

  render: function() {

    var reefTypes = this.props.data.map(function(reeftype) {
      return (
        <ReefType onReefTypeDelete={this.itemDelete} id={reeftype.id} name={reeftype.name} depth={reeftype.depth}/>
      );
    }.bind(this));

    return (
        <ListGroup>
        {reefTypes}
        </ListGroup>
    );
  }
});

var ReefTypes = React.createClass({

  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false
    };
  },

  delete: function(value) {
    $.ajax({
      'type': 'DELETE',
      'url': '/reeftypes/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var reefTypes = this.state.data;
          var remainder = reefTypes
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainder,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those reeftypes you wanted deleted...',
            hasMessage: true
          });
        }
      }.bind(this)
    });

  },

  create: function(value) {
    console.log("create name: " + value.name);

    $.ajax({
      'type': 'PUT',
      'url': '/reeftypes',
      'contentType': 'application/json',
      'data': JSON.stringify({
        'name': value.name,
        'depth': value.code
      }),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var reeftypes = this.state.data;
          var results = reeftypes.concat([data]);
          this.setState({
            data: results,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those reeftypes...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/reeftypes',
      'contentType': 'application/json',
      'async': 'false',
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          this.setState({
            data: data,
            message: '',
            hasMessage: false
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Big Error',
            hasMessage: true
          });
        }
      }.bind(this)
    });

  },

  render: function() {

    return (
      /* jshint ignore:start */
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
             <div className="col-lg-9 page-header">
               <h2>Reef Types <CreateReefTypeTrigger onHandlingData={this.handleCreate}/></h2>
               <hr/>
               <ReefTypeList delete={this.delete} data={this.state.data}/>
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }

});
