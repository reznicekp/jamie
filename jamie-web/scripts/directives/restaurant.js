(function () {
    var app = angular.module('restaurantDirective', []);

    app.directive('restaurant', ['$log', '$http', 'SERVER_URL', function($log, $http, SERVER_URL) {
        return {
            restrict : 'E',
            templateUrl : 'views/restaurant.html',
            transclude : true,
            link : function(scope, element, attrs) {
                scope.cancelAddRestaurant = function(restaurant) {
                    if (scope.insertedRestaurant == restaurant) {
                        scope.developers.forEach(function (developer) {
                            developer.likings.pop();
                        });
                    }
                };

                scope.saveRestaurant = function(data, id) {
                    if (id) {
                        data.id = id;
                        $http.put(SERVER_URL + '/restaurants', data)
                            .then(function() {
                                scope.loadDevelopers();
                            }, function() {
                                $log.error('Updating existing restaurant failed');
                            });
                    } else {
                        $http.post(SERVER_URL + '/restaurants', data)
                            .then(function() {
                                scope.loadDevelopers();
                            }, function() {
                                $log.error('Saving new restaurant failed');
                            });
                    }
                };

                scope.removeRestaurant = function(index) {
                    if (confirm('Really remove restaurant ' + scope.developers[0].likings[index].restaurant.name + '?')) {
                        $http.delete(SERVER_URL + '/restaurants/' + scope.developers[0].likings[index].restaurant.id)
                            .then(function() {
                                scope.developers.forEach(function(developer) {
                                    developer.likings.splice(index, 1);
                                });
                            }, function() {
                                $log.error('Deleting restaurant failed');
                            });
                    }
                };

                scope.checkRestaurantName = function(data) {
                    if (!data) {
                        return 'Name of restaurant is invalid!';
                    }
                };
            }
        }
    }]);
})();