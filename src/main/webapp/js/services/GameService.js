/**
 * created by Serhii Kryvtsov
 */
puzzle.factory('GameService',
    ['$q', '$rootScope', '$window', '$log', function ($q, $rootScope, $window, $log) {
        var API;
        return API = {
            getScore: function () {
                return gapi.client.puzzle.leaders()
            },
            getAllGames: function () {
                return gapi.client.puzzle.getAllGames();
            },
            checkWord: function (reqest) {
                return gapi.client.puzzle.checkWord(reqest);
            }
            , addGame: function (request) {
                return gapi.client.puzzle.save(request);
            }, deleteGame: function (id) {
                return gapi.client.puzzle.delete(id);
            }, getGameById: function (id) {
                $log.debug('Loading game by id: ' + id)
                return gapi.client.puzzle.getGameById({'gameId': id});
            }, submitScore: function (request) {
                return gapi.client.puzzle.submitScore(request);
            }
            , getLeaderboard: function () {
                return gapi.client.puzzle.leaders();
            }
        }
    }]
);