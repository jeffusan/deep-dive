define(function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var ListGroup = ReactBootstrap.ListGroup;
  var ListGroupItem = ReactBootstrap.ListGroupItem;
  var Button = ReactBootstrap.Button;
  var Input = ReactBootstrap.Input;
  var Modal = ReactBootstrap.Modal;
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var ModalTrigger = ReactBootstrap.ModalTrigger;
  var auth = require('auth/auth');
  var DeleteItem = require('jsx!admin/common/delete');
  var Expire = require('jsx!admin/common/expirable');
  var Pager = require('admin/common/pager.min');

  var CreateRegionTrigger = React.createClass({

    handleDataSubmit: function(value) {
      this.props.onHandlingData({name: value.name});
    },

    render: function() {
      return (
        <ModalTrigger modal={<CreateRegion onCreateRegionSubmit={this.handleDataSubmit}/>}>
          <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
            <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
          </button>
        </ModalTrigger>
      );
    }
  });

  var CreateRegionInput = React.createClass({

    getInitialState: function() {
      return {
        value: ''
      };
    },

    handleChange: function(newValue) {
      this.setState({
        value: this.refs.input.getValue()
      });
      this.props.onHandleChange({name: this.refs.input.getValue()});
    },

    render: function() {
      return (
        <Input
          type="text"
          value={this.state.value}
          placeholder="Enter name"
          label="Name of the Region"
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} />
     );
   }
  });

  var CreateRegion = React.createClass({

    getInitialState: function() {
      return {
        value: ''
      };
    },

    updateName: function(nameValue) {
      this.state.value = nameValue;
    },

    handleSubmit: function(event) {
      event.preventDefault();
      var name = this.state.value.name;
      this.props.onCreateRegionSubmit({name: name});
      this.props.onRequestHide();
    },

    render: function() {
      return (
        <Modal title="Add A Region" animation={true}>
          <div className="modal-body">
            <CreateRegionInput onHandleChange={this.updateName} />
             <div className="modal-footer">
               <ButtonToolbar>
                 <Button onClick={this.handleSubmit} bsStyle="primary" bsSize="large">Create</Button>
                 <Button onClick={this.props.onRequestHide}>Close</Button>
               </ButtonToolbar>
             </div>
           </div>
        </Modal>
      );
    }
  });

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
      var message = "Deleting a Region will also delete it's SubRegions, Sites, and Surveys. You will be destroying the world...";

      return (
        <ListGroupItem bsStyle="info">
          <h4><a href="#" id={this.props.id} ref="input">{this.props.name}</a>
            <span className="pull-right">
              <DeleteItem
                title="Are You Sure???"
                message={message}
                delete={this.onRegionDelete}/>
            </span>
          </h4>
        </ListGroupItem>
      );
    }
  });

  var RegionList = React.createClass({

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

    delete: function(value) {
      this.props.delete({id: value.id});
    },

    render: function() {

      var regionNodes = function() {
        var start = this.state.current * this.state.visiblePage;
        var end = 0;
        if((this.props.data.length - start) >= 10) {
          end = start + 10;
        } else {
          end = this.props.data.length;
        }
        var regions = [];
        var sliceOHeaven = this.props.data.slice(start, end);
        for (i = 0; i < sliceOHeaven.length; i++) {
          var reg = sliceOHeaven[i];
          regions.push(<Region onRegionDelete={this.delete} id={reg.id} key={reg.id} name={reg.name}/>);
        }
        return regions;
      }.bind(this);

      return (
          <div>
          <Pager total={this.props.data.length / 10}
            current={this.state.current}
            visiblePages={this.state.visiblePage}
            onPageChanged={this.handlePageChanged}/>
          <ListGroup>
          {regionNodes()}
          </ListGroup>
          </div>
      );
    }
  });

  var Regions = React.createClass({

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
            var remainingRegions = regions.filter(function (reg) {
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

  });

return Regions;
});
