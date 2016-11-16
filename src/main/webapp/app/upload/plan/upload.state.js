(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('upload-plan', {
            parent: 'app',
            url: '/upload/plan',
            data: {
                authorities: ['ROLE_USER'],
                
            },
            views: {
                'content@': {
                    templateUrl: 'app/upload/plan/upload.html',
                    controller: 'PlanUploadController',
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