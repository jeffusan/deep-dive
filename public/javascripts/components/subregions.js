/*jshint strict:false */
/*global React:false*/

var SubRegion = React.createClass({

  itemDelete: function(event) {
    var id = this.props.id;
    console.log("Value: " + id);
    this.props.onSubRegionDelete({id: id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info"><h4>{this.props.name}</h4> ({this.props.regionName})
          <span className="pull-right">
            <DeleteItem
              title="Delete this Subregion?"
              message="Deleting this subregion will also delete it's sites and surveys."
              delete={this.itemDelete}
            />
          </span>
        </ListGroupItem>
    );
  }
});

var SubRegionList = React.createClass({

  itemDelete: function(event) {
    var id = this.props.id;
    this.props.delete(event);
  },

  render: function() {

    var subregions = this.props.data.map(function(subregion) {
      return (
          <SubRegion onSubRegionDelete={this.itemDelete} id={subregion.id} name={subregion.name} regionName={subregion.region.name}/>
      );
    }.bind(this));

    return (
        <ListGroup>
        {subregions}
        </ListGroup>
    );
  }
});

var SubRegions = React.createClass({

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
      'url': '/subregions/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var subRegions = this.state.data;
          var remainingSubRegions = subRegions
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainingSubRegions,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those subregions you wanted deleted...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  handleCreate: function(value) {
    console.log("create name: " + value.name);

    $.ajax({
      'type': 'PUT',
      'url': '/subregions',
      'contentType': 'application/json',
      'data': JSON.stringify({
        'name': value.name,
        'code': value.code,
        'regionId': value.regionId
      }),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var subregions = this.state.data;
          var newSubRegions = subregions.concat([data]);
          this.setState({
            data: newSubRegions,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those subregions...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
  },

  componentDidMount: function() {

    $.ajax({
      'type': 'GET',
      'url': '/subregions',
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
               <h2>Sub Regions <CreateSubRegionTrigger onHandlingData={this.handleCreate}/></h2>
               <hr/>
               <SubRegionList delete={this.delete} data={this.state.data}/>
             </div>
          </div>
        </div>
      </div>
      /* jshint ignore:end */
    );
  }

});
