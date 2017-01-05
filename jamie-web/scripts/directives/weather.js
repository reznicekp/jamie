(function () {
    var app = angular.module('weatherDirective', []);

    app.directive('weather', ['$log', '$http', 'SERVER_URL', function($log, $http, SERVER_URL) {
        return {
            restrict : 'E',
            templateUrl : 'views/weather.html',
            link : function(scope, element, attrs) {
                $http.get(SERVER_URL + '/weatherforecast/now')
                    .then(function (response) {
                        scope.weatherForecastNow = response.data;
                        scope.weatherForecastNow.rain = scope.weatherForecastNow.rain && Object.values(scope.weatherForecastNow.rain)[0] ? Object.values(scope.weatherForecastNow.rain)[0] + ' mm' : '-';
                        scope.weatherForecastNow.snow = scope.weatherForecastNow.snow && Object.values(scope.weatherForecastNow.snow)[0] ? Object.values(scope.weatherForecastNow.snow)[0] + ' mm' : '-';
                    }, function () {
                        $log.error('Loading weather forecast for now failed');
                    });
                $http.get(SERVER_URL + '/weatherforecast/tomorrow')
                    .then(function (response) {
                        scope.weatherForecastTomorrow = response.data;
                        scope.weatherForecastTomorrow.rain = scope.weatherForecastTomorrow.rain && Object.values(scope.weatherForecastTomorrow.rain)[0] ? Object.values(scope.weatherForecastTomorrow.rain)[0] + ' mm' : '-';
                        scope.weatherForecastTomorrow.snow = scope.weatherForecastTomorrow.snow && Object.values(scope.weatherForecastTomorrow.snow)[0] ? Object.values(scope.weatherForecastTomorrow.snow)[0] + ' mm' : '-';
                    }, function () {
                        $log.error('Loading weather forecast for tomorrow failed');
                    });
            }
        }
    }]);
})();