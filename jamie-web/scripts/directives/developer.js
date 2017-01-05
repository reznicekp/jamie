(function () {
    var app = angular.module('developerDirective', []);

    app.directive('developer', ['$log', '$http', 'SERVER_URL', function($log, $http, SERVER_URL) {
        return {
            restrict : 'E',
            templateUrl : 'views/developer.html',
            transclude : true,
            link : function(scope, element, attrs) {
                scope.cancelAddDeveloper = function(developer) {
                    if (scope.insertedDeveloper == developer) {
                        scope.developers.pop();
                    }
                };

                scope.saveDeveloper = function(data, id) {
                    if (id) {
                        data.id = id;
                        $http.put(SERVER_URL + '/developers', data)
                            .then(function() {
                                scope.loadDevelopers();
                            }, function() {
                                $log.error('Updating existing developer failed');
                            });
                    } else {
                        $http.post(SERVER_URL + '/developers', data)
                            .then(function() {
                                scope.loadDevelopers();
                            }, function() {
                                $log.error('Saving new developer failed');
                            });
                    }
                };

                scope.removeDeveloper = function(index) {
                    if (confirm('Really remove developer ' + scope.developers[index].name + '?')) {
                        $http.delete(SERVER_URL + '/developers/' + scope.developers[index].id)
                            .then(function() {
                                scope.developers.splice(index, 1);
                            }, function() {
                                $log.error('Deleting developer failed');
                            });
                    }
                };

                scope.checkDeveloperName = function(data) {
                    if (!data) {
                        return 'Name of developer is invalid!';
                    }
                };
            }
        }
    }]);
})();