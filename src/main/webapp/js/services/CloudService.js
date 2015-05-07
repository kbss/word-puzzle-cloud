/**
 * created by Serhii Kryvtsov
 */
puzzle.factory('CloudService',
    ['$q', '$rootScope', '$window', '$log', function ($q, $rootScope, $window, $log) {
        var API, isReady = false;
        $window.initGapi = function () {
            $log.debug('Google API loaded');
            var rootApi = '//' + window.location.host + '/_ah/api';
            gapi.client.load('puzzle', 'v1', function () {
                $log.debug("Puzzle API loaded");
                API.promise.resolve();
            }, rootApi);
        };
        return API = {
            init: function () {
                API.promise = $q.defer();
                var script = document.createElement("script");
                script.src = 'https://apis.google.com/js/client.js?onload=initGapi';
                script.type = "text/javascript";
                script.onerror = function () {
                    $log.warn('apis.google.com/js/client Script Failed Loading');
                    API.promise.reject("Failed to load gapi script");
                };
                document.body.appendChild(script);
                script = null;
                return API.promise.promise;
            }
            , isReady: function () {
                return isReady;
            }
            , promise: undefined
        }
    }]
);