/*jshint strict:false */
/*global React:false */
var Modal = ReactBootstrap.Modal;
var ModalTrigger = ReactBootstrap.ModalTrigger;
var Badge = ReactBootstrap.Label;

var EditableTextInput = React.createClass({

  componentDidMount: function() {
    $('#' + this.props.elementid).editable({
      type: 'text',
      pk: this.props.id,
      url: this.props.url,
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

  render: function() {
    return (
        <a href="#" id={this.props.elementid} ref="input">{this.props.name}</a>
    );

  }
});

var DeleteItem = React.createClass({

  render: function() {
    return (
      <div >
        <ModalTrigger modal={<DeleteModal container={this} />} container={this}>
        <Badge
          regionId={this.props.id}
          id="edit-delete-badge"
          className="selectable"
          bsStyle="danger">Delete</Badge>
        </ModalTrigger>
      </div>
    );
  }
});

var DeleteModal = React.createClass({

  delete: function(event) {
    this.props.container.props.delete({
      id: event.id
    });
    this.props.onRequestHide();
  },

  render: function() {

    return (
        <Modal {...this.props} title={this.props.container.props.title} animation={false}>
        <div className="modal-body">
        {this.props.container.props.message}
        </div>
        <div className="modal-footer">
        <Button bsStyle="danger" onClick={this.delete}>Danger Zone</Button>
          <Button onClick={this.props.onRequestHide}>Cancel</Button>
        </div>
      </Modal>
    );
  }

});
