define(function(require){

  var React = require('react');
  var Router = require('react-router');
  var Login = require('jsx!auth/login');
  var Authentication = require('auth/authentication');
  var DashboardNav = require('jsx!admin/dashboardNav');
  var Empty = require('jsx!admin/empty');
  var Regions = require('jsx!admin/regions');
  var SubRegions = require('jsx!admin/subregions');
  var ReefTypes = require('jsx!admin/reeftypes');
  var Sites = require('jsx!admin/sites');
  var BenthicUpload = require('jsx!admin/benthic/uploads');
  var SurveyEvents = require('jsx!admin/surveyevents');
  var BenthicCategories = require('jsx!admin/benthic/categories');
  var BenthicSubCategories = require('jsx!admin/benthic/subcategories');
  var Users = require('jsx!admin/users');

  var Dashboard = React.createClass({
    mixins: [ Authentication, Router.State ],

    render: function () {

      var makeTheSausage = function(selection) {

        if(! selection) {
          return <Empty/>;
        } else {
          var page;

          switch(selection) {
            case 'users': page = <Users/>; break;
            case 'regions': page = <Regions/>; break;
            case 'subregions': page = <SubRegions/>; break;
            case 'reeftypes': page = <ReefTypes/>; break;
            case 'sites': page = <Sites/>; break;
            case 'surveyevents': page = <SurveyEvents/>; break;
            case 'upload': page = <BenthicUpload/>; break;
            case 'benthiccategories': page = <BenthicCategories/>; break;
            case 'benthicsubcategories': page = <BenthicSubCategories/>; break;
            default: page = <Empty/>; break;
          }
          return page;
        }
      };

      $("#top").removeClass('header')
      $("body").addClass('header-dashboard');

      return (
        <div id="wrapper">
          <DashboardNav/>
          {makeTheSausage(this.getParams().selection)}
        </div>
      );
    }
  });

  return Dashboard;

});
