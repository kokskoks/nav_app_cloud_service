(function() {
    'use strict';
    angular
        .module('navAppApp')
        .factory('Version', Version);

    Version.$inject = ['$resource'];

    function Version ($resource) {
        var resourceUrl =  'api/versions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
