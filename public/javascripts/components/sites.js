/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */
/*global ReactBootstrap:false*/

var ListGroup = ReactBootstrap.ListGroup;
var ListGroupItem = ReactBootstrap.ListGroupItem;
var Button = ReactBootstrap.Button;
var Input = ReactBootstrap.Input;

var Site = React.createClass({
  onSiteDelete: function(event) {
    var id = this.props.id;
    this.props.onSiteDelete({id: id});
  },
  render: function() {
    return (
        <ListGroupItem bsStyle="info">
          <h4><a href="#" id={this.props.id} ref="input">{this.props.name}</a>
            <span className="pull-right">
              <DeleteItem
      title="Are You Sure???"
      message="Deleting a Site will also delete it's Survey Events!"
      delete={this.onSiteDelete}/>
            </span>
          </h4>
        </ListGroupItem>
    );
  }
});

var SiteList = React.createClass({

  delete: function(value) {
    this.props.delete({id: value.id});
  },

  render: function() {
    var siteNodes = this.props.data.map(function(site){
      return (
          <Site
        onSiteDelete={this.delete}
        id={site.id}
        name={site.name}
        latitude={site.latitude}
        longitude={site.longitude}
        map_datum={site.map_datum}
        subregion_id={site.subregion_id}/>
      );
    }.bind(this));

    return (
        <ListGroup>
        {siteNodes}
      </ListGroup>
    );
  }
});

var Sites = React.createClass({

  getInitialState: function() {
    return {
      data: []
    };
  },

  delete: function(value) {
    $.ajax({
      'type': 'DELETE',
      'url': '/sites/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var sites = this.state.data;
          var remainingSites = sites
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainingSites,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those sites you wanted canceled...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  handleCreate: function(value) {

    $.ajax({
      'type': 'PUT',
      'url': '/sites',
      'contentType': 'application/json',
      'data': JSON.stringify({
        'name': value.name,
        'latitude': value.latitude,
        'longitude': value.longitude,
        'map_datum': value.map_datum,
        'reef_type_id': value.reef_type_id,
        'region_id': value.region_id
      }),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var sites = this.state.data;
          var newSites = sites.concat([data]);
          this.setState({
            data: newSites,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those sites...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/sites',
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

    var maybeMessage = this.state.hasMessage ?
          <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
          <span />;

    return (
      /* jshint ignore:start */
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <h3 id='errors'>{this.state.mess}</h3>
             <div className="col-lg-9 page-header">
        <h2>Sites         <CreateSiteTrigger
      onHandlingData={this.handleCreate}/></h2>
               <hr/>
               {maybeMessage}
               <SiteList delete={this.delete} data={this.state.data} />
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }

});
