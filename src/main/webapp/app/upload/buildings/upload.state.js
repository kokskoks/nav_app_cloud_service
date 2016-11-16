(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('upload-buildings', {
            parent: 'app',
            url: '/upload/buildings',
            data: {
                authorities: ['ROLE_USER'],
                
            },
            views: {
                'content@': {
                    templateUrl: 'app/upload/buildings/upload.html',
                    controller: 'BuildingsUploadController',
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