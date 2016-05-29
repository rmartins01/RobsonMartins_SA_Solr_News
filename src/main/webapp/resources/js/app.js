'use strict';

var AngularSpringApp = {};
var onSelectNewsArticle = false;
var App = angular.module('AngularSpringApp', ['ngRoute', 'autocomplete', 'AngularSpringApp.filters', 'AngularSpringApp.services', 'AngularSpringApp.directives']);

// Declare app level module which depends on filters, and services
App.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/news', {
        templateUrl: 'news/layout',
        controller: NewsArticleController
    });

    $routeProvider.otherwise({redirectTo: '/news'});
}]);
