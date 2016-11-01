(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('upload', {
            parent: 'app',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER'],
                
            },
            views: {
                'content@': {
                    templateUrl: 'app/upload/upload.html',
                    controller: 'UploadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('upload');
                    return $translate.refresh();
                }]
            }
        });
    }
})();