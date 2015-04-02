define(function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var Expire = require('jsx!admin/common/expirable');
  var ModalTrigger = ReactBootstrap.ModalTrigger;
  var ListGroup = ReactBootstrap.ListGroup;
  var ListGroupItem = ReactBootstrap.ListGroupItem;
  var auth = require('auth/auth');
  var EditableTextInput = require('jsx!admin/common/editable');
  var DeleteItem = require('jsx!admin/common/delete');
  var Modal = ReactBootstrap.Modal;
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var Button = ReactBootstrap.Button;
  var Input = ReactBootstrap.Input;
  var Pager = require('admin/common/pager.min');

  var CreateReefTypeTrigger = React.createClass({

    handleDataSubmit: function(value) {
      this.props.onHandlingData({
        name: value.name,
        depth: value.depth
      });
    },

    render: function() {
      return (
        <ModalTrigger modal={<CreateReefType onCreateReefType={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
          <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        </ModalTrigger>
      );
    }
  });

  var CreateReefTypeNameInput = React.createClass({

    getInitialState: function() {
      return {
        name: ''
      };
    },

    handleChange: function(newValue) {
      this.setState({
        name: this.refs.input.getValue()
      });
      this.props.onHandleChange({name: this.refs.input.getValue()});
    },

    render: function() {
      return (
        <Input
          type="text"
          value={this.state.name}
          placeholder="Enter name"
          label="Name of the Reef Type"
          hasFeedback
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} />
      );
    }
  });

  var CreateReefTypeDepthInput = React.createClass({

    getInitialState: function() {
      return {
        depth: ''
      };
    },

    handleChange: function(newValue) {
      this.setState({
        depth: this.refs.input.getValue()
      });
      this.props.onHandleChange({depth: this.refs.input.getValue()});
    },

    render: function() {
      return (
        <Input
      type="text"
      value={this.state.depth}
      placeholder="Enter depth"
      label="Depth of the Reef Type"
      hasFeedback
      ref="input"
      groupClassName="group-class"
      wrapperClassName="wrapper-class"
      labelClassName="label-class"
      onChange={this.handleChange} />
    );
  }

});

var CreateReefType = React.createClass({

  getInitialState: function() {
    return {
      name: '',
      depth: ''
    };
  },

  updateName: function(nameValue) {
    this.state.name = nameValue;
  },

  updateDepth: function(depthValue) {
    this.state.depth = depthValue;
  },

  handleSubmit: function(event) {
    event.preventDefault();
    var name = this.state.name.name;
    var depth = this.state.depth.depth;
    this.props.onCreateReefType({
      name: name,
      depth: depth
    });
    this.props.onRequestHide();
  },

  render: function() {
    var noOp = function() {};
    return (
        <Modal title="Add a Reef Type" animation={true} onRequestHide={noOp}>
        <div className="modal-body">
        <CreateReefTypeNameInput onHandleChange={this.updateName}  />
        <CreateReefTypeDepthInput onHandleChange={this.updateDepth} />
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

  var ReefType = React.createClass({

    componentDidMount: function() {
      $('#depth' + this.props.id).editable({
        type: 'text',
        pk: this.props.id,
        url: '/reeftypesdepth',
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
      this.props.onReefTypeDelete({id: id});
    },
    render: function() {
      return (
        <ListGroupItem bsStyle="info">
        <h4>
        <div className="pull-left reef-name-edit">
        <EditableTextInput id={this.props.id} name={this.props.name} url="reeftypesname" elementid={"name" + this.props.id}/>
        </div>
        (<EditableTextInput id={this.props.id} name={this.props.depth} url="reeftypesdepth" elementid={"depth" + this.props.id}/>)
          <span className="pull-right">
           <DeleteItem
             title="Delete this ReefType?"
             message="Deleting this reeftype may be constrained by its association with Sites."
             delete={this.itemDelete} />
        </span>
        </h4>
        </ListGroupItem>
      );
    }
  });

  var ReefTypeList = React.createClass({

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

    itemDelete: function(event) {
      var id = this.props.id;
      this.props.delete(event);
    },
    render: function() {

      var rts = function() {
        var start = this.state.current * this.state.visiblePage;
        var end = 0;
        if((this.props.data.length - start) >= 10) {
          end = start + 10;
        } else {
          end = this.props.data.length;
        }
        var s = [];
        var sliceOHeaven = this.props.data.slice(start, end);
        for (i = 0; i < sliceOHeaven.length; i++) {
         var reeftype = sliceOHeaven[i];
         s.push(
        <ReefType
          onReefTypeDelete={this.itemDelete}
          id={reeftype.id}
          key={reeftype.id}
          name={reeftype.name}
          depth={reeftype.depth}/>

         );
        }
        return s;
      }.bind(this);

      return (
        <div>
          <Pager total={this.props.data.length / 10}
            current={this.state.current}
            visiblePages={this.state.visiblePage}
            onPageChanged={this.handlePageChanged}/>
        <ListGroup>
        {rts()}
        </ListGroup>
        </div>
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
            var remainder = reefTypes.filter(function (reg) {
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

      $.ajax({
      'type': 'PUT',
      'url': '/reeftypes',
      'contentType': 'application/json',
      'data': JSON.stringify({
        'name': value.name,
        'depth': value.depth
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

    var maybeMessage = this.state.hasMessage ?
          <Expire visible={true} delay={4000}>{this.state.message}</Expire> :
          <span />;

    return (

        <div className="container-fluid">
        <div className="panel panel-default">
          <div className="panel-heading clearfix">
        <h3 className="panel-title pull-left">Reef Types <CreateReefTypeTrigger onHandlingData={this.create}/></h3>
        </div>
        <div className="panel-body">
        <ReefTypeList delete={this.delete} data={this.state.data} />
        </div>
        <div className="panel-footer">
        {maybeMessage}
      </div>
        </div>
        </div>
      );
    }
  });

  return ReefTypes;
});