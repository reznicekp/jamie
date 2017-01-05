(function () {
    var app = angular.module('likingDirective', []);

    app.directive('liking', ['$log', '$http', 'SERVER_URL', function($log, $http, SERVER_URL) {
        return {
            restrict : 'E',
            templateUrl : 'views/liking.html',
            transclude : true,
            link : function(scope, element, attrs) {
                scope.changeLiking = function (liking, points) {
                    var newLiking = {
                        id : liking.id,
                        value : liking.value + points
                    };
                    $http.put(SERVER_URL + '/likings', newLiking)
                        .then(function () {
                            liking.value = newLiking.value;
                        }, function () {
                            $log.error('Updating liking failed');
                        });
                };
            }
        }
    }]);
})();