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
                $log.debug('Adding new game');
                request = {
                    "content": "VNYBKGSRORANGEETRNXWPLAEALKAPMHNWMRPOCAXBGATNOMEL",
                    "name": "Simple game 1",
                    "words": [
                        "banana",
                        "APPLE",
                        "LEMON",
                        "ORANGE",
                        "GRAPES",
                        "CHERRY"
                    ]
                }
                return gapi.client.puzzle.add(request);
            }
        }
    }]
);