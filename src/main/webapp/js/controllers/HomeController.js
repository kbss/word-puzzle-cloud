/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('HomeController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'CloudService', 'GameService', function ($scope, $rootScope, $log, $location, $window, CloudService, GameService) {

        $scope.isLoading = true;
        $scope.games = [];
        $scope.score = [];
        $scope.init = function () {
            var promise = CloudService.init();
            promise.then(function () {
                $scope.isLoading = false;

                GameService.getScore().then(function (resp) {
                    $log.debug(resp.result.items);
                }, function (err) {
                    $log.error(err);
                });
                GameService.getAllGames().then(function (resp) {
                    $log.debug('Loaded games: ' + resp.result.items.length);
                    $scope.games = resp.result.items;
                    $scope.$apply();
                }, function (err) {
                    $log.error('Err:' + err);
                });

            }, function (rejectedMessage) {
                $log.warn(rejectedMessage);

            })['finally'](function () {
                $scope.isLoading = false;
            });
        }
        $scope.init();

    }]
);