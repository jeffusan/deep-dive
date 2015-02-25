/*jshint strict:false */
/*global React:false*/

var SubRegionList = React.createClass({

  itemDelete: function(event) {
    this.props.delete(event);
  },

  render: function() {

    var subregions = this.props.data.map(function(subregion) {
      return (
        <ListGroupItem bsStyle="info"><h4>{subregion.name}</h4> ({subregion.region.name})
          <span className="pull-right">
            <DeleteItem
              title="Delete this Subregion?"
              message="Deleting this subregion will also delete it's sites and surveys."
        delete={this.itemDelete}
        id={subregion}
            />
          </span>
        </ListGroupItem>
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

  delete: function(event) {
    console.log("deleting");
  },
  getInitialState: function() {
    return {
      data: [],
      message: '',
      hasMessage: false
    };
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
               <h2>Sub Regions</h2>
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
