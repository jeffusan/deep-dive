define(function (require) {
    var React = require('react');
    var ReactBootstrap = require('react-bootstrap');
    var Panel = ReactBootstrap.Panel;
    var auth = require('auth/auth');
    var Row = ReactBootstrap.Row;
    var Col = ReactBootstrap.Col;
    var ButtonToolbar = ReactBootstrap.ButtonToolbar;
    var Button = ReactBootstrap.Button;
    var Input = ReactBootstrap.Input;

    var AddSite = React.createClass({

        getInitialState: function () {
            return {
                subregions: [],
                reeftypes: [],
                localName: '',
                latitude: '',
                longitude: '',
                mapDatum: '',
                reefType: 0,
                subRegion: 0
            };
        },

        handleSubmit: function (event) {
            event.preventDefault();
            console.log("event: " + this.state.localName);
            return {};
        },

        componentDidMount: function () {

            $.ajax({
                'type': 'GET',
                'url': '/subregions',
                'contentType': 'application/json',
                'async': 'false',
                'headers': {
                    'X-XSRF-TOKEN': auth.getToken()
                },
                'success': function (data) {
                    if (this.isMounted()) {
                        this.setState({
                            subregions: data
                        });
                    }
                }.bind(this),
                'error': function (data) {
                    if (this.isMounted()) {
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
                'success': function (data) {
                    if (this.isMounted()) {
                        this.setState({
                            reeftypes: data
                        });
                    }
                }.bind(this),
                'error': function (data) {
                    if (this.isMounted()) {
                        this.setState({
                            reeftypes: []
                        });
                    }
                }.bind(this)
            });
        },

        updateLocalName: function(localName) {
            this.state.localName = localName;
        },

        updateReefType: function(reefType) {
            this.state.reefType = reefType;
        },

        updateLongitude: function(longitude) {
            this.state.longitude = longitude;
        },

        updateLatitude: function(latitude) {
            this.state.latitude = latitude;
        },

        updateSubRegion: function(subregion) {
            this.state.subregion = subregion;
        },

        updateMapDatum: function(mapdatum) {
            this.state.mapDatum = mapdatum;
        },

        render: function () {
            const title = (
                <h4>Add a Site</h4>
            );

            var subregionator = this.state.subregions.map(function (sr) {
                return (
                    <option key={sr.id} value={sr.id}>{sr.name}</option>
                );
            }.bind(this));

            var reefinator = this.state.reeftypes.map(function (rt) {
                return (
                    <option key={rt.id} value={rt.id}>{rt.name}</option>
                )
            }.bind(this));

            return (
                <Panel header={title} className="add-site-panel">
                    <form className="addSiteForm" onSubmit={this.handleSubmit}>
                        <Row className='site-grid-row'>
                            {this.renderTextInput('localName', this.state.localName, 'Local Name (optional)', this.updateLocalName)}
                            {this.renderDropDownInput('reeftype', 'Reef Type', reefinator, this.updateReefType)}
                            {this.renderTextInput('longitude', this.state.longitude, 'Longitude', this.updateLongitude)}
                        </Row>
                        <Row className='site-grid-row'>
                            {this.renderTextInput('latitude', this.state.latitude, 'Latitude', this.updateLatitude)}
                            {this.renderDropDownInput('subregion', 'Sub Region', subregionator, this.updateSubRegion)}
                            {this.renderTextInput('mapDatum', this.state.mapDatum, 'Map Datum', this.updateMapDatum)}
                        </Row>
                        <Row>
                            <ButtonToolbar>
                                <Button onClick={this.handleSubmit} bsStyle='primary' bsSize='large'>Add Site</Button>
                            </ButtonToolbar>
                        </Row>
                    </form>
                </Panel>
            );
        },

        renderDropDownInput: function (id, label, data, onChange) {
            return (
                <div>
                    <Col xs={6} md={3}>
                        <Input type='select'
                               placeholder='select'
                               label={label}
                               hasFeedback
                               ref={id}
                               groupClassName="group-class"
                               wrapperClassName="wrapper-class"
                               labelClassName="label-class"
                                onHandleChange={onChange}>
                            <option value='0'></option>
                            {data}
                        </Input>
                    </Col>
                </div>
            );
        },

        renderTextInput: function (id, state, label, onChange) {
            return (
                <div>
                    <Col xs={12} md={3}>
                        <Input
                            type="text"
                            value={state}
                            label={label}
                            groupClassName="group-class"
                            wrapperClassName="wrapper-class"
                            labelClassName="label-class"
                            onHandleChange={onChange}/>
                        </Col>
                </div>
            );
        }
    });

    return AddSite;
});
