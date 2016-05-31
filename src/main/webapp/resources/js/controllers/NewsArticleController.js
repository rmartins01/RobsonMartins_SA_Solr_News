'use strict';

var NewsArticleController = function($scope, $http) {
    $scope.news = {};
    $scope.typeaheadNames = {};

    $scope.fetchNamesList = function() {
        $http.get('news/getList.json').success(function(list){
            $scope.news = list;
            $scope.search = '';
        });
    };

    $scope.searchNamesList = function(text) {
        
        $http.post('news/search.json', text).success(function(list){
            $scope.news = list;
        });
        
    };

    $scope.autocompleteNames = function(text){
        $http.post('news/autocomplete.json', text).success(function(list){
            $scope.typeaheadNames = list;
        });
    };

    $scope.add = function(newItem) {
        $scope.resetError();

        $http.post('news/add', newItem).success(function() {
            $scope.fetchNamesList();
            $scope.name.title = '';
            $scope.name.text_content = '';
        }).error(function() {
            $scope.setError('Could not add a new content');
        });
    };

    $scope.removeName = function(id) {
        $scope.resetError();

        $http.delete('news/remove/' + id).success(function() {
            $scope.fetchNamesList();
        }).error(function() {
            $scope.setError('Could not remove content');
        });
        $scope.name.title = '';
        $scope.name.text_content = '';
    };

    $scope.removeAllNames = function() {
        $scope.resetError();

        $http.delete('news/removeAll').success(function() {
            $scope.fetchNamesList();
        }).error(function() {
            $scope.setError('Could not remove all news');
        });
    };

    $scope.resetNamesForm = function() {
        $scope.resetError();
        $scope.news = {};
        $scope.search = '';
        $scope.editMode = false;
    };

    $scope.resetError = function() {
        $scope.error = false;
        $scope.errorMessage = '';
    };

    $scope.setError = function(message) {
        $scope.error = true;
        $scope.errorMessage = message;
    };

    $scope.fetchNamesList();

    $scope.predicate = 'id';
};