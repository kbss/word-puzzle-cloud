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
                    $scope.score = resp.items;
                }, function (err) {
                    $log.error(err);
                    $rootScope.isLoading = false;
                    NotificationService.error(err);
                })['finally'](function () {
                    $rootScope.isLoading = false;
                });
            }
        }
        API.getScore();
    }]
);