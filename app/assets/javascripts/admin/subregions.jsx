define(function(require) {

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var auth = require('auth/auth');
  var ModalTrigger = ReactBootstrap.ModalTrigger;
  var ListGroup = ReactBootstrap.ListGroup;
  var ListGroupItem = ReactBootstrap.ListGroupItem;
  var DeleteItem = require('jsx!common/delete');
  var Pager = require('common/pager.min');
  var Modal = ReactBootstrap.Modal;
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var Button = ReactBootstrap.Button;
  var Input = ReactBootstrap.Input;
  var Expire = require('jsx!common/expirable');
  var Pager = require('common/pager.min');  

  var CreateSubRegionTrigger = React.createClass({

    handleDataSubmit: function(value) {
      this.props.onHandlingData({
        name: value.name,
        code: value.code,
        regionId: value.regionId
      });
    },

    render: function() {
      return (
        <ModalTrigger modal={<CreateSubRegion onCreateSubRegion={this.handleDataSubmit}/>}>
        <button onClick={this.handleClick} type="button" className="btn btn-default" aria-label="Left Align">
          <span className="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        </ModalTrigger>
      );
    }
  });

  var CreateSubRegionNameInput = React.createClass({

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
          label="Name of the SubRegion"
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} />
      );
    }
  });

  var CreateSubRegionCodeInput = React.createClass({

    getInitialState: function() {
      return {
        code: ''
      };
    },

    handleChange: function(newValue) {
      this.setState({
        code: this.refs.input.getValue()
      });
      this.props.onHandleChange({code: this.refs.input.getValue()});
    },

    render: function() {
      return (
        <Input
          type="text"
          value={this.state.code}
          placeholder="Enter code"
          label="Code of the SubRegion"
          ref="input"
          groupClassName="group-class"
          wrapperClassName="wrapper-class"
          labelClassName="label-class"
          onChange={this.handleChange} />
      );
    }

  });

  var CreateRegionSelector = React.createClass({

    getInitialState: function() {
      return {
        regionId: '',
        regions: []
      };
    },

    handleChange: function(event) {
      this.setState({
        regionId: this.refs.input.getValue()
      });
      this.props.onHandleChange({regionId: this.refs.input.getValue()});
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
              regions: data
            });
          }
        }.bind(this),
        'error': function(data) {
          if(this.isMounted()) {
            this.setState({
              regions: []
            });
          }
        }.bind(this)
      });
    },

    render: function() {
      var regionator = this.state.regions.map(function(region) {
        return (
          <option key={region.id} value={region.id}>{region.name}</option>
        );
      }.bind(this));

      return (
        <Input type="select" label='Select' defaultValue="select" ref="input" onChange={this.handleChange}>
          <option value="0"></option>
          {regionator}
        </Input>
      );
    }
  });

  var CreateSubRegion = React.createClass({

    getInitialState: function() {
      return {
        name: '',
        code: '',
        regionId: ''
      };
    },

    updateName: function(nameValue) {
      this.state.name = nameValue;
    },

    updateCode: function(codeValue) {
      this.state.code = codeValue;
    },

    updateRegion: function(regionId) {
      this.state.regionId = regionId;
    },

    handleSubmit: function(event) {
      event.preventDefault();
      var name = this.state.name.name;
      var code = this.state.code.code;
      var regionId = this.state.regionId.regionId;
      this.props.onCreateSubRegion({
        name: name,
        code: code,
        regionId: regionId
      });
      this.props.onRequestHide();
    },

    render: function() {
      var noOp = function () {};
      return (
        <Modal title="Add a Sub Region" animation={true} onRequestHide={noOp}>
        <div className="modal-body">
        <CreateSubRegionNameInput onHandleChange={this.updateName}  />
        <CreateSubRegionCodeInput onHandleChange={this.updateCode} />
        <CreateRegionSelector onHandleChange={this.updateRegion} />
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

      var su = this.props.data.map(function(subregion) {
        return (
        <SubRegion
          onSubRegionDelete={this.itemDelete}
          id={subregion.id}
          key={subregion.id}
          name={subregion.name}
          regionName={subregion.region.name}/>
        );
      }.bind(this));

      var subregions = function() {

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
          var reg = sliceOHeaven[i];
          s.push(
          <SubRegion
          onSubRegionDelete={this.itemDelete}
          id={reg.id}
          key={reg.id}
          name={reg.name}
          regionName={reg.region.name}/>
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
          {subregions()}
        </ListGroup>
        </div>
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

  return SubRegions;
});