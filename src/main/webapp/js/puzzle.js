/**
 * @author Serhii Krivtsov
 */
var puzzle = angular.module("puzzle", ['ngRoute', 'webStorageModule'])
    .config(
    ['$locationProvider',
        function ($locationProvider) {
            $locationProvider.html5Mode(true);
            $locationProvider.hashPrefix('!');
        }
    ])
    .run(
    ['$rootScope',
        function ($rootScope) {
            $rootScope.$on("routeChangeStart", function (event) {
                $rootScope.hideGlobalLoader = false;
            });

            $rootScope.$on("$routeChangeError", function (event) {

            });

            $rootScope.$on("$routeChangeSuccess", function (event, next, current) {
                $rootScope.hideGlobalLoader = true;
            });
            $rootScope.$on('$viewContentLoaded', function () {

            });
        }])
    .config(
    ['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when("/", {
                templateUrl: 'templates/home.page.html',
                controller: 'HomeController'
            }).when("/404", {
                templateUrl: 'templates/404.html',
                controller: '404Controller'
            }).otherwise({
                redirectTo: '/404'
            });
        }

    ])

    // Disables GET requests caching
    .config(
    ['$httpProvider',
        function ($httpProvider) {
            if (!$httpProvider.defaults.headers.get) {
                $httpProvider.defaults.headers.get = {};
            }

            $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';

            $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
            $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        }
    ]);

