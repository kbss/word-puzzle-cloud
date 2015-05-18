/**
 * created by Serhii Kryvtsov
 */
puzzle.controller('HomeController',
    ['$scope', '$rootScope', '$log', '$location', '$window', 'GameService', 'NotificationService', function ($scope, $rootScope, $log, $location, $window, GameService, NotificationService) {

        $rootScope.isLoading = true;
        $scope.games = [];
        $scope.score = [];

        $scope.board = [];
        $scope.words = [];
        $scope.selected = [];
        $scope.size = 0;
        $scope.result = {
            startTime: undefined,
            endTime: undefined,
            score: undefined
        }
        $scope.score = 0;


        $scope.menu = {
            start: true,
            list: false,
            score: false,
            info: false,
            game: false
        }
        var API = {
            getGames: function () {
                GameService.getAllGames().then(function (resp) {
                    $log.debug('Loaded games: ' + resp.items.length);
                    $scope.games = resp.items;
                }, function (err) {
                    $log.error('Err:' + err);
                    NotificationService.error(err);
                })['finally'](function () {
                    $rootScope.isLoading = false;
                });

            }, clearSelection: function () {
                for (var i = 0; i < $scope.selected.length; i++) {
                    $scope.selected[i].selected = false;
                }
                $scope.selected = [];
            }
        }

        $scope.selectGame = function () {
            $scope.menu.start = false;
            $scope.menu.list = true;
        }

        $scope.showMenu = function () {
            $scope.menu.start = true;
            $scope.menu.list = false;
            $scope.menu.score = false;
            $scope.menu.info = false;
            $scope.menu.game = false

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
            if ($scope.selected.length == 2) {
                API.clearSelection();
            }
            $scope.selected[$scope.selected.length] = $scope.board[x][y];
            $scope.board[x][y].selected = true;

            if ($scope.selected.length == 2) {
                var p1 = $scope.selected[0];
                var p2 = $scope.selected[1];
                var request = {
                    gameId: $scope.game.id,
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
                    var points = resp.points;
                    var word = resp.word;
                    var allPassed = true;
                    for (var i = 0; i < $scope.words.length; i++) {
                        if ($scope.words[i].text == word) {
                            $scope.words[i].passed = true;
                        }
                        allPassed &= $scope.words[i].passed;
                    }
                    if (allPassed) {
                        $scope.result.endTime = new Date().getTime();
                        //Calculate score
                        $scope.result.score = Math.round(($scope.size * $scope.game.words.length * 1000) * 1000 / ($scope.result.endTime - $scope.result.startTime));
                    }
                    for (var i = 0; i < points.length; i++) {
                        $scope.board[points[i].x][points[i].y].passed = true;
                    }
                    $scope.$apply();
                }, function (err) {
                    API.clearSelection();
                    $log.error(err)

                });
            }
        }
        $scope.startGame = function (id) {
            for (var i = 0; i < $scope.games.length; i++) {
                if (id == $scope.games[i].id) {
                    $scope.game = $scope.games[i];
                    $scope.menu.list = false;
                    $scope.menu.game = true
                    break;
                }
            }
            $scope.size = $scope.game.board.length;
            $scope.board = [];
            for (var i = 0; i < $scope.game.board.length; i++) {
                var chars = $scope.game.board[i].split('');
                $scope.board[i] = [];
                for (var j = 0; j < chars.length; j++) {
                    $scope.board[i][j] = {char: chars[j], selected: false, x: i, y: j};
                }
            }
            $scope.words = [];
            for (var i = 0; i < $scope.game.words.length; i++) {
                $scope.words[i] = {text: $scope.game.words[i].toUpperCase(), valid: false};
            }
            $scope.result.startTime = new Date().getTime();
            $scope.result.endTime = 0;
            $scope.result.score = 0;
        }
        $scope.showGame = function (id) {
            $location.path('game/' + id);
        }

        API.getGames();
    }]
);