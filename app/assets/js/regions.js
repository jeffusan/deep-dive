var Regions = React.createClass({

  render: function() {
    return (
        <div id="page-wrapper">
            <div className="container-fluid">
                <div className="row">
                    <div className="col-lg-12">
                        <h1 className="page-header">
                            Region Page
                            <small>Subheading</small>
                        </h1>
                        <ol className="breadcrumb">
                            <li>
                                <i className="fa fa-dashboard"></i>  <a href="index.html">Regions</a>
                            </li>
                            <li className="active">
                                <i className="fa fa-file"></i> Regions Page
                            </li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    );
  }
});
