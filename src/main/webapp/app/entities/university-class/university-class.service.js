(function() {
    'use strict';
    angular
        .module('navAppApp')
        .factory('UniversityClass', UniversityClass);

    UniversityClass.$inject = ['$resource'];

    function UniversityClass ($resource) {
        var resourceUrl =  'api/university-classes/:id';

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
