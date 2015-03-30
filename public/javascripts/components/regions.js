/*jshint strict:false */
/*global React:false */
/*global auth:false */
/*global $:false */
/*global ReactBootstrap:false*/

var ListGroup = ReactBootstrap.ListGroup;
var ListGroupItem = ReactBootstrap.ListGroupItem;
var Button = ReactBootstrap.Button;
var Input = ReactBootstrap.Input;

var Region = React.createClass({

  componentDidMount: function() {
      $('#' + this.props.id).editable({
      type: 'text',
      pk: this.props.id,
      url: '/regions',
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

  onRegionDelete: function(event) {
    var id = this.props.id;
    console.log("Value: " + id);
    this.props.onRegionDelete({id: id});
  },

  render: function() {
    return (
        <ListGroupItem bsStyle="info">
          <h4><a href="#" id={this.props.id} ref="input">{this.props.name}</a>
            <span className="pull-right">
              <DeleteItem
      title="Are You Sure???"
      message="Deleting a Region will also delete it's SubRegions, Sites, and Surveys. You will be destroying the world..."
      delete={this.onRegionDelete}/>
            </span>
          </h4>
        </ListGroupItem>
    );
  }
});

var RegionList = React.createClass({

  delete: function(value) {
      this.props.delete({id: value.id});
    },

  render: function() {

    var regionNodes = this.props.data.map(function (region) {
      return (
          <Region onRegionDelete={this.delete} id={region.id} name={region.name}/>
      );
    }.bind(this));

    return (
      <ListGroup>
        {regionNodes}
      </ListGroup>
    );
  }
});

var Regions = React.createClass({

  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false,
      pagination: {
        page: 0,
        perPage: 5
      }
    };
  },

  delete: function(value) {
    $.ajax({
      'type': 'DELETE',
      'url': '/regions/' + value.id,
      'contentType': 'application/json',
      'data': JSON.stringify({'id': value.id}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var regions = this.state.data;
          var remainingRegions = regions
               .filter(function (reg) {
                 return reg.id !== value.id;
               });
          this.setState({
            data: remainingRegions,
            message: 'Gone!',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those regions you wanted canceled...',
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
      'url': '/regions',
      'contentType': 'application/json',
      'data': JSON.stringify({'name': value.name, 'depth': value.depth}),
      'dataType': 'json',
      'async': false,
      'headers': {
        'X-XSRF-TOKEN': auth.getToken()
      },
      'success' : function(data) {
        if(this.isMounted()) {
          var regions = this.state.data;
          var newRegions = regions.concat([data]);
          this.setState({
            data: newRegions,
            message: 'Roger that',
            hasMessage: true
          });
        }
      }.bind(this),
      'error': function(data) {
        if(this.isMounted()) {
          this.setState({
            data: {},
            message: 'Um, yeah. About those regions...',
            hasMessage: true
          });
        }
      }.bind(this)
    });
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

    var data = this.state.data || [];
    var pagination = this.state.pagination || {};
//    var paginated = Paginator.paginate(data, pagination);

    var maybeMessage = this.state.hasMessage ?
          <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
          <span />;

    return (
        <div className="container-fluid">
        <div className="panel panel-default">
          <div className="panel-heading clearfix">
        <h3 className="panel-title pull-left">Regions <CreateRegionTrigger onHandlingData={this.handleCreate}/></h3>
        </div>
        <div className="panel-body">
        <RegionList className='data' delete={this.delete} data={this.state.data} />
        </div>
        <div className="panel-footer">
        {maybeMessage}
      </div>
        </div>
        </div>
    );
  },

  onSelect(page) {
    var pagination = this.state.pagination || {};

    pagination.page = page;

    this.setState({
      pagination: pagination
    });
  },

  onPerPage(e) {
    var pagination = this.state.pagination || {};

    pagination.perPage = parseInt(event.target.value, 10);

    this.setState({
      pagination: pagination
    });
  }
});
