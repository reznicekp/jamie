(function () {
    var app = angular
        .module('jamieApp', ['xeditable', 'weatherDirective', 'restaurantDirective', 'developerDirective', 'likingDirective', 'dailyPreferenceDirective'])
        .constant('SERVER_URL', 'http://127.0.0.1:8080');

    app.controller('MainController', ['$scope', '$http', '$log', 'SERVER_URL', function ($scope, $http, $log, SERVER_URL) {
        $scope.developers = [];
        $scope.joiningDevelopersIds = [];
        $scope.dailyPreferences = [];
        $scope.calculationInProgress = false;
        $scope.todaysChoice = null;

        $scope.initApp = function() {
            $scope.loadDevelopers();
            loadTodaysDailyChoice();
        };

        $scope.loadDevelopers = function() {
            $http.get(SERVER_URL + '/developers')
                .then(function (response) {
                    $scope.developers = response.data;
                }, function () {
                    $log.error('Loading developers failed');
                });
        };

        var loadTodaysDailyChoice = function() {
            $http.get(SERVER_URL + '/history?date=' + new Date().toISOString().slice(0, 10))
                .then(function(response) {
                    $scope.updateDailyChoice(response.data);
                }, function() {
                    $log.error('Loading daily choice for today failed');
                });
        };

        $scope.addDeveloper = function() {
            var newLikings = [];
            $scope.developers[0].likings.forEach(function(liking) {
                var newLiking = {
                    id : null,
                    restaurant : liking.restaurant,
                    value : 0
                };
                newLikings.push(newLiking);
            });
            $scope.insertedDeveloper = {
                id : null,
                name : '',
                usuallyJoins : true,
                likings : newLikings
            };
            $scope.developers.push($scope.insertedDeveloper);
        };

        $scope.addRestaurant = function() {
            $scope.insertedRestaurant = {
                id : null,
                name : '',
                distance : 1
            };
            $scope.developers.forEach(function(developer) {
                developer.likings.push({
                    id : null,
                    restaurant : $scope.insertedRestaurant,
                    value : 0
                });
            });
        };

        $scope.calculatePreferences = function () {
            $scope.calculationInProgress = true;
            $scope.joiningDevelopersIds = $scope.developers
                .map(function (developer) {
                    if (developer.usuallyJoins) {
                        return developer.id;
                    }
                })
                .filter(function(developerId) {
                    return developerId != undefined;
                });

            $http.get(SERVER_URL + '/dailypreferences?developers=' + $scope.joiningDevelopersIds.toLocaleString())
                .then(function(response) {
                    $scope.dailyPreferences = response.data;
                    $scope.dailyPreferences.forEach(function(dailyPreference) {
                        dailyPreference.historyDay = new Date();
                    });
                    $scope.calculationInProgress = false;
                }, function () {
                    $log.error('Calculation of daily preferences failed');
                    $scope.calculationInProgress = false;
                });
        };

        $scope.updateDailyChoice = function(dailyChoice) {
            $scope.todaysChoice = dailyChoice;
        };
    }]);
})();
