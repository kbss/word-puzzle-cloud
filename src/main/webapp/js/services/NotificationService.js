/**
 * created by Serhii Kryvtsov
 */
puzzle.factory('NotificationService',
    ['$rootScope', function ($rootScope) {

        var API;
        $rootScope.message = {
            title: null,
            message: null,
            error: false,
            info: true
        };
        return API =
        {
            error: function (message, title) {

                $rootScope.message.text = message;
                $rootScope.message.error = true;
                $rootScope.message.info = false;
                if (title) {
                    $rootScope.message.title = title;
                } else {
                    $rootScope.message.title = 'Error';
                }
                $rootScope.$apply();
                $('#message').modal();
            }, info: function (message, title) {

            $rootScope.message.text = message;
            $rootScope.message.error = false;
            $rootScope.message.info = true;
            if (title) {
                $rootScope.message.title = title;
            } else {
                $rootScope.message.title = 'Info';
            }
            $rootScope.$apply();
            $('#message').modal();
        }
        }
    }]
);