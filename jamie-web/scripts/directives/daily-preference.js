(function () {
    var app = angular.module('dailyPreferenceDirective', []);

    app.directive('dailyPreference', ['$log', '$http', 'SERVER_URL', function($log, $http, SERVER_URL) {
        return {
            restrict : 'E',
            templateUrl : 'views/daily-preference.html',
            transclude : true,
            link : function(scope, element, attrs) {
                scope.chooseRestaurant = function(dailyPreference) {
                    var joiningDevelopers = scope.joiningDevelopersIds.map(function(developerId) {
                        return {
                            id : developerId
                        };
                    });
                    var historyItem = {
                        restaurant : {
                            id : dailyPreference.id
                        },
                        developers : joiningDevelopers
                    };

                    if (scope.todaysChoice) {
                        if (confirm('Really change todays choice?')) {
                            historyItem.id = scope.todaysChoice.id;
                            $http.put(SERVER_URL + '/history', historyItem)
                                .then(function (response) {
                                    scope.updateDailyChoice(response.data);
                                }, function () {
                                    $log.error('Saving history failed');
                                });
                        }
                    } else {
                        $http.post(SERVER_URL + '/history', historyItem)
                            .then(function(response) {
                                scope.updateDailyChoice(response.data);
                            }, function() {
                                $log.error('Saving history failed');
                            });
                    }
                };
            }
        }
    }]);
})();