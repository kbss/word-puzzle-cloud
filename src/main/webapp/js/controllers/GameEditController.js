/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('GameEditController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'GameService', 'NotificationService', function ($scope, $rootScope, $log, $location, $window, GameService, NotificationService) {

        $rootScope.isLoading = true;
        $scope.games = [];
        $scope.game = {};
        var gameMap;
        $scope.gameList = true;
        $scope.gameEdit = false;
        $scope.board = [];

        var API = {
            init: function () {
                API.getGames();
            },
            saveGame: function () {
                $log.debug($scope.game);
                $rootScope.isLoading = true;
                GameService.addGame($scope.game).then(function (resp) {
                    $log.debug(resp.items);
                    API.getGames();
                    $scope.gameList = true;
                    $scope.gameEdit = false;
                }, function (err) {
                    $log.error(err);
                    NotificationService.error(err);
                })['finally'](function () {
                    $rootScope.isLoading = false;
                });
            },
            deleteGame: function () {
                $rootScope.isLoading = true;
                GameService.deleteGame({'id': $scope.game.id}).then(function (resp) {
                    $log.debug(resp);
                    API.getGames();
                }, function (err) {
                    $log.error(err);
                })['finally'](function () {
                    $rootScope.isLoading = false;
                });
            },
            getGames: function () {
                $rootScope.isLoading = true;
                GameService.getAllGames().then(function (resp) {
                    $log.debug('Loaded games: ' + resp.items.length);
                    $scope.games = resp.items;
                    gameMap = {};
                    for (var i = 0; i < $scope.games.length; i++) {
                        var game = $scope.games[i];
                        gameMap[game.id] = game;
                    }
                    $rootScope.isLoading = false;
                }, function (err) {
                    $log.error('Err:' + err);
                })['finally'](function () {
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

        API.init();

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
            $scope.puzzleChanged();
        }


        $scope.addWord = function () {
            //$scope.game.words[$scope.game.words.length] = '';
            $log.debug('Adding new word');
            $scope.game.words.push('');
        }

        $scope.saveGame = function ($form) {

            if (!$form.$valid) {
                return;
            }
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

        $scope.puzzleChanged = function () {

            if (!$scope.game.content) {
                $scope.board = [];
                return;
            }
            var size = Math.round(Math.sqrt($scope.game.content.length));
            if (size < 2) {
                $scope.board = [];
                return;
            }
            var content = $scope.game.content.split('');
            $log.debug(size);
            $scope.board = [];
            var charIndex = 0;
            for (var i = 0; i < size; i++) {
                if ($scope.board[i] == null) {
                    $scope.board[i] = [];
                }
                //$scope.board[0][i];
                for (var j = 0; j < size; j++) {
                    $scope.board[i][j] = content[charIndex++];
                }
            }
            $log.debug($scope.board);


        }
    }
    ]
)
;