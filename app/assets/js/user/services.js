/**
 * User service, exposes user model to the rest of the app.
 */
define([
  'angular',
  'angular-couch-potato',
  'angular-cookies',
  'common/module',
  'user/module'
], function (angular, couchPotato) {
  'use strict';

  var module = angular.module('user.services', [
    'ngCookies',
    'app.common'
  ]);

  couchPotato.configureApp(module);
  
  module.registerFactory('userService', ['$http', '$q', 'playRoutes', '$cookies', '$log', function ($http, $q, playRoutes, $cookies, $log) {
    var user, token = $cookies['XSRF-TOKEN'];

    /* If the token is assigned, check that the token is still valid on the server */
    if (token) {
      $log.info('Restoring user from cookie...');
      playRoutes.controllers.Users.user().get()
        .success(function (data) {
          $log.info('Welcome back, ' + data.name);
          user = data;
        })
        .error(function () {
          $log.info('Token no longer valid, please log in.');
          token = undefined;
          delete $cookies['XSRF-TOKEN'];
          return $q.reject("Token invalid");
        });
    }

    return {
      loginUser: function (credentials) {
        return playRoutes.controllers.Application.login().post(credentials).then(function (response) {
          // return promise so we can chain easily
          token = response.data.authToken;
          return response.data.user;
        }).then(function (response) {
          user = response.data;
          return user;
        });
      },
      logout: function () {
        // Logout on server in a real app
        delete $cookies['XSRF-TOKEN'];
        token = undefined;
        user = undefined;
        return playRoutes.controllers.Application.logout().post().then(function () {
          $log.info("Good bye");
        });
      },
      getUser: function () {
        return user;
      }
    };
  }]);

  /**
   * Add this object to a route definition to only allow resolving the route if the user is
   * logged in. This also adds the contents of the objects as a dependency of the controller.
   */
  module.constant('userResolve', {
    user: ['$q', 'userService', function ($q, userService) {
      var deferred = $q.defer();
      var user = userService.getUser();
      if (user) {
        deferred.resolve(user);
      } else {
        deferred.reject();
      }
      return deferred.promise;
    }]
  });

  /**
   * If the current route does not resolve, go back to the start page.
   */
  module.run(['$rootScope', '$location', function ($rootScope, $location) {
    $rootScope.$on('$routeChangeError', function (/*e, next, current*/) {
      $location.path('/');
    });
  }]);

  module.run(['$couchPotato', function ($couchPotato) {
    module.lazy = $couchPotato;
  }]);

  return module;
});
