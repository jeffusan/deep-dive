define(function(require){
  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var Panel = ReactBootstrap.Panel;
  var auth = require('auth/auth');
  var Grid = ReactBootstrap.Grid;
  var Row = ReactBootstrap.Row;
  var Col = ReactBootstrap.Col;
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var Button = ReactBootstrap.Button;
  var Input = ReactBootstrap.Input;

  var AddSite = React.createClass({

    getInitialState: function() {
      return {
        subregions: [],
        reeftypes: []
      }
    },

    handSubmit: function() {
      return {};
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
              subregions: data
            });
          }
        }.bind(this),
        'error': function(data) {
          if(this.isMounted()) {
            this.setState({
              subregions: []
            });
          }
        }.bind(this)
      });

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
              reeftypes: data
            });
          }
        }.bind(this),
        'error': function(data) {
          if(this.isMounted()) {
            this.setState({
              reeftypes: []
            });
          }
        }.bind(this)
      });
    },

    render: function() {
      const title = (
          <h4>Add a Site</h4>
      );

      var subregionator = this.state.subregions.map(function(sr) {
        return (
            <option key={sr.id} value={sr.id}>{sr.name}</option>
        );
      }.bind(this));

      var reefinator = this.state.reeftypes.map(function(rt) {
        return (
            <option key={rt.id} value={rt.id}>{rt.name}</option>
        )
      }.bind(this));

      return (
          <Panel header={title} className="add-site-panel">
          <form className="addSiteForm" onSubmit={this.handleSubmit}>
          <Row className='site-grid-row'>
          {this.renderTextInput('name', 'Name')}
          {this.renderTextInput('latitude', 'Longitude')}
          </Row>
          <Row className='site-grid-row'>
          {this.renderDropDownInput('reeftype', 'Reef Type', reefinator)}
          {this.renderTextInput('longitude', 'Longitude')}
          </Row>
          <Row className='site-grid-row'>
          {this.renderDropDownInput('subregion', 'Sub Region', subregionator)}
          {this.renderTextInput('mapDatum', 'Map Datum')}
          </Row>
          <Row>
          <ButtonToolbar >
          <Button bsStyle='primary' bsSize='large'>Add Site</Button>
          </ButtonToolbar>
          </Row>
          </form>
          </Panel>
      );
    },

    renderDropDownInput: function(id, label, data) {
      return (
        <div>
          <Col xs={6} md={1}>
          <label htmlFor={id} className="control-label">{label}</label>
          </Col>
          <Col xs={6} md={3}>
          <Input type='select' placeholder='select'>
          <option value='0'></option>
          {data}
          </Input>
          </Col>
          <Col xs={6} md={2}/>
        </div>
      );
    },

    renderTextInput: function(id, label) {
      return (
        <div>
          <Col xs={6} md={1}>
          <label htmlFor={id} className="control-label">{label}</label>
          </Col>
          <Col xs={6} md={3}>
          <input type="text" className="form-control" id={id} ref={id} />
          </Col>
          <Col xs={6} md={2}/>
        </div>
      );
    }

  });


  return AddSite;
});
