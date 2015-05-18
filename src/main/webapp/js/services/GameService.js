/**
 * created by Serhii Kryvtsov
 */
puzzle.factory('GameService',
    ['$q', '$parse', '$log', 'CloudService', function ($q, $parse, $log, CloudService) {
        var API, endpoint = 'gapi.client.puzzle.';

        return API = {
            getScore: function () {
                return API.getPromise(endpoint + 'leaders');
            },
            getAllGames: function () {
                return API.getPromise(endpoint + 'getAllGames');
            },
            checkWord: function (request) {
                return API.getPromise(endpoint + 'checkWord', request);
            }
            , addGame: function (request) {
                return API.getPromise(endpoint + 'save', request);
            }, deleteGame: function (id) {
                return API.getPromise(endpoint + 'delete', id);
            }, getGameById: function (gameId) {
                return API.getPromise(endpoint + 'getGameById', {'gameId': gameId});
            }, submitScore: function (request) {
                return API.getPromise(endpoint + 'submitScore', request);
            }, getPromise: function (funct, arg) {
                var deferred = $q.defer();
                var execute = function () {
                    eval(funct)(arg).then(function (resp) {
                        deferred.resolve(resp.result);
                    }, function (err) {
                        var message = err.result.error.errors[0].message;
                        $log.error(message);
                        deferred.reject(message)
                    });
                }
                if (!CloudService.isReady()) {
                    var promise = CloudService.init();
                    promise.then(function () {
                        execute();
                    }, function (rejectedMessage) {
                        $log.error(rejectedMessage);
                        deferred.reject(rejectedMessage);
                    });
                } else {
                    execute();
                }
                return deferred.promise;
            }
        }
        API.init();
    }]
);