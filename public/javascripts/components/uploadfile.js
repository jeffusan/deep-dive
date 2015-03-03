var UploadImageForm = React.createClass({
  getInitialState: function() {
    return {
      myFileName: "",
      myFileHandle: {}
    };
  },

  handleChange: function(event) {
    console.log("handleChange() fileName = " + event.target.files[0].name);
    console.log("handleChange() file handle = " + event.target.files[0]);
    this.setState( {myFileName: event.target.files[0].name} );
    this.setState( {myFileHandle: event.target.files[0]} );
  },

  handleSubmit: function(e) {
    e.preventDefault();
    console.log("INSIDE: handleSubmit()");
    console.log("fileName = " + this.state.myFileName);
    console.log("this.state.myFileHandle = " + this.state.myFileHandle);


    if (this.state.myFileHandle) {
      console.log("INSIDE if test myFileHandle.length");
      var file = this.state.myFileHandle;
      var name = this.state.myFileName;
      var parseFile = new Parse.File(name, file);


      var myUser = new Parse.Object("TestObj");
      myUser.set("profilePicFile", parseFile);
      myUser.save()
        .then(function() {
          // The file has been saved to User.
          this.setState( {myFileHandle: null} );
          console.log("FILE SAVED to Object: Parse.com");
        }.bind(this), function(error) {
          // The file either could not be read, or could not be saved to Parse.
          console.log("ERROR: Parse.com " + error.code + " " + error.message);
        });;
    } // end if

  },

  render: function() {
      return  (

        <form onSubmit={this.handleSubmit}>
          <input type="file" onChange={this.handleChange} id="profilePhotoFileUpload" />
          <input type="submit" value="Post" />
        </form>
      );
  }
});
