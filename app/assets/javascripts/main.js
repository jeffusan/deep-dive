// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    // Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['auth', 'admin', 'common'],
    paths: {
      'react': ['../lib/react/react-with-addons'],
      'JSXTransformer': ['../lib/jsx-requirejs-plugin/js/JSXTransformer'],
      'jsx': ['../lib/jsx-requirejs-plugin/js/jsx'],
      'jquery': ['../lib/jquery/jquery'],
      'react-router' : ['../lib/react-router/ReactRouter'],
      'jsRoutes': ['/jsroutes'],
      'react-router-shim': 'react-router-shim',
      'text': ['../lib/requirejs-text/text'],
      'bootstrap': ['../lib/bootstrap/js/bootstrap'],
      'react-bootstrap': ['../lib/react-bootstrap/react-bootstrap'],
      'bootstrap-editable': ['../lib/x-editable-bootstrap3/js/bootstrap-editable'],
      'bootstrap-datepicker': ['../lib/bootstrap-datepicker/js/bootstrap-datepicker']
    },
    jsx: {
      fileExtension: '.jsx'
    },
    shim : {
      'jsRoutes': {
        deps: [],
        // it's not a RequireJS module, so we have to tell it what var is returned
        exports: 'jsRoutes'
      },
      'react': {
        deps: ['jquery'],
        exports: 'react'
      },
      'react-router': {
        deps:    ['react'],
        exports: 'Router'
      },
      'bootstrap': ['jquery'],
      'react-bootstrap': {
        deps: ['react', 'bootstrap'],
        exports: 'ReactBootstrap'
      }
    }
  });

  requirejs.onError = function (err) {
    console.log(err);
  };

  require(['jsx!app'], function(App){

    App.init();

  });

})(requirejs);
