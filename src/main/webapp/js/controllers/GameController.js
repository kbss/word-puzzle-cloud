/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('GameController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'GameService', 'gameInfo', 'NotificationService', function ($scope, $rootScope, $log, $location, $window, GameService, gameInfo, NotificationService) {

        $scope.board = [];
        $scope.words = [];
        $scope.selected = [];
        $scope.size = 0;
        $scope.result = {
            startTime: undefined,
            endTime: undefined,
            score: undefined,
            playerName: 'Player1'
        }
        $scope.showScore = false;
        var API = {
            clearSelection: function () {
                for (var i = 0; i < $scope.selected.length; i++) {
                    $scope.selected[i].selected = false;
                }
                $scope.selected = [];
            }, selectChar: function (x, y) {

                $log.debug($scope.selected.length);
                if ($scope.selected.length == 2) {
                    API.clearSelection();
                }
                $scope.selected[$scope.selected.length] = $scope.board[x][y];
                $scope.board[x][y].selected = true;
                if ($scope.selected.length == 2) {
                    var request = {
                        gameId: gameInfo.id,
                        points: [
                            {
                                x: $scope.selected[0].x,
                                y: $scope.selected[0].y
                            },
                            {
                                x: $scope.selected[1].x,
                                y: $scope.selected[1].y
                            }
                        ],
                        word: null

                    }
                    API.clearSelection();
                    GameService.checkWord(request).then(function (resp) {
                        $log.debug(resp.result.points);
                        var points = resp.result.points;
                        var word = resp.result.word;
                        var allPassed = true;
                        for (var i = 0; i < $scope.words.length; i++) {
                            if ($scope.words[i].text == word) {
                                if ($scope.words[i].passed) {
                                    API.clearSelection();
                                    return;
                                }
                                $scope.words[i].passed = true;
                            }
                            allPassed &= $scope.words[i].passed;
                        }
                        if (allPassed) {
                            $scope.result.endTime = new Date().getTime();
                            $scope.result.score = Math.round(($scope.size * $scope.size * 10 * gameInfo.words.length * 10) * 1000 / ($scope.result.endTime - $scope.result.startTime));
                            if ($scope.result.score < 1) {
                                $scope.result.score = 1;
                            }
                            $scope.showScore = true;
                        }
                        for (var i = 0; i < points.length; i++) {
                            $scope.board[points[i].x][points[i].y].passed = true;
                        }

                        $scope.$apply();
                    }, function (err) {
                        API.clearSelection();
                        $scope.selected[$scope.selected.length] = $scope.board[x][y];
                        $scope.board[x][y].selected = true;
                        $log.error(err.result.error.errors[0].message)
                        $scope.$apply();
                    });
                }
            }, startGame: function () {
                $log.debug('Start game');
                $scope.playerName = "";
                $scope.showScore = false;
                $scope.size = gameInfo.board.length;
                $scope.board = [];
                for (var i = 0; i < gameInfo.board.length; i++) {
                    var chars = gameInfo.board[i].split('');
                    $scope.board[i] = [];
                    for (var j = 0; j < chars.length; j++) {
                        $scope.board[i][j] = {char: chars[j], selected: false, x: i, y: j};
                    }
                }
                $scope.words = [];
                for (var i = 0; i < gameInfo.words.length; i++) {
                    $scope.words[i] = {text: gameInfo.words[i].toUpperCase(), valid: false};
                }
                $scope.result.startTime = new Date().getTime();
                $scope.result.endTime = 0;
                $scope.result.score = 0;
            }, navigateToScore: function () {
                $log.debug('Redirecting to score page')
                $location.path('score/');
                $scope.$apply();
            }
        }

        $scope.selectChar = function (x, y) {
            API.selectChar(x, y);
        }
        $scope.submitScore = function () {
            $rootScope.isLoading = true;
            var request = {
                id: gameInfo.id,
                score: $scope.result.score,
                name: $scope.result.playerName
            }
            GameService.submitScore(request).then(function (resp) {
                $rootScope.isLoading = false;
                API.navigateToScore();
            }, function (err) {
                $log.error(err.result.error.errors[0].message);
                $rootScope.isLoading = false;
                NotificationService.error(err.result.error.errors[0].message);
                API.navigateToScore();
            });
        }
        API.startGame();
    }]
);