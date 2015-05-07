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
            }
            , addGame: function (request) {
                $log.debug('Adding new game')
                request = {
                    "content": "gdfhdfhdfhdfgvdfbdfhdfhdfh",
                    "name": "test1",
                    "words": [
                        "word1",
                        "word2",
                        "word3"
                    ]
                }
                return gapi.client.puzzle.add(request);
            }
        }
    }]
);