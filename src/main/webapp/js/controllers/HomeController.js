/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('HomeController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'CloudService', 'GameService', function ($scope, $rootScope, $log, $location, $window, CloudService, GameService) {

        $scope.isLoading = true;
        $scope.games = [];
        $scope.score = [];
        $scope.game = {};
        $scope.board = [];

        $scope.selected = [];


        $scope.menu = {
            start: true,
            list: false,
            score: false,
            info: false,
            game: false
        }
        var API = {
            init: function () {
                var promise = CloudService.init();
                promise.then(function () {
                    API.getScore();
                    API.getGames();
                }, function (rejectedMessage) {
                    $log.warn(rejectedMessage);
                })['finally'](function () {
                    $scope.isLoading = false;
                });
            },
            getScore: function () {
                GameService.getScore().then(function (resp) {
                    $log.debug(resp.result.items);
                }, function (err) {
                    $log.error(err);
                });
            },
            getGames: function () {
                GameService.getAllGames().then(function (resp) {
                    $log.debug('Loaded games: ' + resp.result.items.length);
                    $scope.games = resp.result.items;

                    $scope.$apply();
                }, function (err) {
                    $log.error('Err:' + err);
                });
            }, clearSelection: function () {
                for (var i = 0; i < $scope.selected.length; i++) {
                    $scope.selected[i].selected = false;
                }
                $scope.selected = [];
            }
        }
        API.init();

        $scope.selectGame = function () {
            $scope.menu.start = false;
            $scope.menu.list = true;
        }

        $scope.showMenu = function () {
            $scope.menu.start = true;
            $scope.menu.list = false;
            $scope.menu.score = false;
            $scope.menu.info = false
        }

        $scope.showScore = function () {
            $scope.menu.start = false;
            $scope.menu.score = true
        }

        $scope.showInfo = function () {
            $scope.menu.start = false;
            $scope.menu.info = true
        }


        $scope.selectChar = function (x, y) {
            $log.debug($scope.selected.length);
            if ($scope.selected.length == 2) {
                API.clearSelection();
            }
            $scope.selected[$scope.selected.length] = $scope.board[x][y];
            $scope.board[x][y].selected = true;
            $log.debug(x + 'x' + y);
        }

        $scope.startGame = function (id) {


            for (var i = 0; i < $scope.games.length; i++) {
                if (id == $scope.games[i].id) {
                    $log.debug('asfasf' + $scope.games[i].name);
                    $scope.game = $scope.games[i];
                    $scope.menu.list = false;
                    $scope.menu.game = true
                    break;
                }
            }
            for (var i = 0; i < $scope.game.board.length; i++) {
                var chars = $scope.game.board[i].split('');
                $scope.board[i] = [];
                for (var j = 0; j < chars.length; j++) {
                    $scope.board[i][j] = {char: chars[j], selected: false};
                }
            }
        }
    }]
);