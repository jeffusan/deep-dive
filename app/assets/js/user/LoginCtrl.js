define(['user/module'], function (module) {

  'use strict';

  return module.registerController('LoginCtrl', ['$scope', '$location', 'userService', function ($scope, $location, userService) {
    $scope.credentials = {};

    $scope.login = function(credentials) {
      userService.loginUser(credentials).then(function(/*user*/) {
        $location.path('/');
      });
    };

    $scope.logout = function() {
      userService.logoutUser().then(function(/*user*/) {
        $location.path('/login');
      });
    };
  }]);

});
