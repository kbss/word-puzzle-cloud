/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('ScoreController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'GameService', 'NotificationService', function ($scope, $rootScope, $log, $location, $window, GameService, NotificationService) {
        $scope.score = [];
        var API = {
            getScore: function () {
                $rootScope.isLoading = true;
                GameService.getScore().then(function (resp) {
                    $rootScope.isLoading = false;
                    $log.debug(resp);
                    $scope.score = resp.result.items;
                    $scope.$apply();
                }, function (err) {
                    $log.error(err.result.error.errors[0].message);
                    $rootScope.isLoading = false;
                    NotificationService.error(err.result.error.errors[0].message);
                    $scope.$apply();
                });
            }
        }
        API.getScore();
    }]
);