(function() {
    'use strict';
    angular
        .module('navAppApp')
        .factory('UniversityGroup', UniversityGroup);

    UniversityGroup.$inject = ['$resource'];

    function UniversityGroup ($resource) {
        var resourceUrl =  'api/university-groups/:id';

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
