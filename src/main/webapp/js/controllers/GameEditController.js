/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('GameEditController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'CloudService', 'GameService', 'NotificationService', function ($scope, $rootScope, $log, $location, $window, CloudService, GameService, NotificationService) {

        $rootScope.isLoading = true;
        $scope.games = [];
        $scope.game = {};
        var gameMap;
        $scope.gameList = true;
        $scope.gameEdit = false;

        var API = {
            saveGame: function () {
                $log.debug($scope.game);

                GameService.addGame($scope.game).then(function (resp) {
                    $log.debug(resp.result.items);
                    API.getGames();
                    $scope.gameList = true;
                    $scope.gameEdit = false;
                }, function (err) {
                    $log.error(err.result.error.errors[0].message);
                    $rootScope.isLoading = false;
                    NotificationService.error(err.result.error.errors[0].message);

                });
            },
            deleteGame: function () {
                GameService.deleteGame({'id': $scope.game.id}).then(function (resp) {
                    $log.debug(resp);
                    API.getGames();
                }, function (err) {
                    $rootScope.isLoading = false;
                    $log.error(err);
                });
            },
            getGames: function () {
                $rootScope.isLoading = true;
                GameService.getAllGames().then(function (resp) {
                    $log.debug('Loaded games: ' + resp.result.items.length);
                    $scope.games = resp.result.items;

                    gameMap = {};

                    for (var i = 0; i < $scope.games.length; i++) {
                        var game = $scope.games[i];
                        gameMap[game.id] = game;
                    }
                    $rootScope.isLoading = false;
                    $scope.$apply();

                }, function (err) {
                    $log.error('Err:' + err);
                    $rootScope.isLoading = false;
                });
            }, selectGame: function (id) {
                $scope.game = gameMap[id];
                if ($scope.game == null) {
                    $log.error('Game with id: ' + id + 'not found');
                    return;
                }
                $scope.game['content'] = '';
                for (var i = 0; i < $scope.game.board.length; i++) {
                    $scope.game['content'] += $scope.game.board[i];
                }
                $log.debug($scope.game);
            }, showEdit: function () {
                $scope.gameList = false;
                $scope.gameEdit = true;
            }
        }

        API.getGames();

        $scope.showGameList = function () {
            $scope.gameList = true;
            $scope.gameEdit = false;
        }
        $scope.confirmDeletion = function (id) {
            API.selectGame(id);
            if ($scope.game == null) {
                return;
            }
            $('#deleteConfirm').modal();
        }

        $scope.delete = function () {
            API.deleteGame();
            $('#deleteConfirm').modal('hide');
        }

        $scope.edit = function (id) {
            API.selectGame(id);
            if ($scope.game == null) {
                return;
            }

            API.showEdit();
        }


        $scope.addWord = function () {
            //$scope.game.words[$scope.game.words.length] = '';
            $log.debug('Adding new word');
            $scope.game.words.push('');
        }

        $scope.saveGame = function () {
            API.saveGame();
        }

        $scope.addNewGame = function () {
            $scope.game = {name: "", content: "", words: ['']};
            API.showEdit();
        }
        $scope.removeWord = function (index) {
            $log.debug('Removing word:' + index)
            $scope.game.words.splice(index, 1);
        }
    }]
);