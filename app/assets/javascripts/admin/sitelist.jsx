define( function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var auth = require('auth/auth');
  var ModalTrigger = ReactBootstrap.ModalTrigger;
  var ListGroup = ReactBootstrap.ListGroup;
  var ListGroupItem = ReactBootstrap.ListGroupItem;
  var Pager = require('admin/common/pager.min');
  var DeleteItem = require('jsx!admin/common/delete');

  var Site = React.createClass({

    itemDelete: function(event) {
      var id = this.props.id;
      this.props.onSiteDelete({id:id});
    },

    render: function() {
      return (
        <ListGroupItem bsStyle="info">
          <h4><a href="#" id={this.props.key} ref="input">{this.props.site_id} - {this.props.name}</a>
        <span className="pull-right">
        <DeleteItem
          title="Delete this Site?"
          message="Deleting this site will also delete surveys."
          delete={this.itemDelete} />
        </span>
        </h4>
      </ListGroupItem>
      );
    }

  });

  var SiteList = React.createClass({

    itemDelete: function(event) {
      this.props.delete({id: event.id});
    },

    getInitialState: function() {
      return {
        total: 0,
        current: 0,
        visiblePage: 10
      }
    },

    handlePageChanged: function ( newPage ) {
      this.setState({ current : newPage });
    },

    render: function() {

      var sites = function() {

        var start = this.state.current * this.state.visiblePage;
        var end = 0;
        if((this.props.data.length - start) >= 10) {
          end = start + 10;
        } else {
          end = this.props.data.length;
        }
        var siteSlice = [];

        var sliceOHeaven = this.props.data.slice(start, end);

        for (i = 0; i < sliceOHeaven.length; i++) {
          var site  = sliceOHeaven[i];
          siteSlice.push(
          <Site
          onSiteDelete={this.itemDelete}
            id={site.id}
            site_id={site.site_id}
            name={site.name}/>
          );
        }
        return siteSlice;

      }.bind(this);

      return (
        <div>
           <Pager total={this.props.data.length / 10}
              current={this.state.current}
              visiblePages={this.state.visiblePage}
              onPageChanged={this.handlePageChanged}/>
        <ListGroup>
          {sites()}
        </ListGroup>
        </div>
      );
    }

  });

  return SiteList;
});
