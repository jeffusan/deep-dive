/*jshint strict:false */
/*global React:false*/

var SubRegion = React.createClass({

  componentDidMount: function() {
      $('#' + this.props.id).editable({
      type: 'text',
      pk: this.props.id,
      url: '/subregions',
      ajaxOptions: {
        headers: {
          'X-XSRF-TOKEN': auth.getToken()
        },
        'dataType': 'json',
        'contentType': 'application/json'
      },
      params: function(params) { return JSON.stringify(params); }
    });
  },

  itemDelete: function(event) {
    var id = this.props.id;
    this.props.onSubRegionDelete({id: id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info">
        <h4><a href="#" id={this.props.id} ref="input">{this.props.name}</a> ({this.props.regionName})
        <span className="pull-right">
        <DeleteItem
          title="Delete this Subregion?"
          message="Deleting this subregion will also delete it's sites and surveys."
          delete={this.itemDelete} />
        </span>
        </h4>
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
      <SubRegion
        onSubRegionDelete={this.itemDelete}
        id={subregion.id}
        name={subregion.name}
        regionName={subregion.region.name}/>
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

        var maybeMessage = this.state.hasMessage ?
          <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
          <span />;


    return (
        <div className="container-fluid">
        <div className="panel panel-default">
          <div className="panel-heading clearfix">
        <h3 className="panel-title pull-left">Sub Regions <CreateSubRegionTrigger onHandlingData={this.handleCreate}/></h3>
        </div>
        <div className="panel-body">
        <SubRegionList delete={this.delete} data={this.state.data} />
        </div>
        <div className="panel-footer">
        {maybeMessage}
      </div>
        </div>
        </div>

    );
  }

});
