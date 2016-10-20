(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('university-group', {
            parent: 'entity',
            url: '/university-group',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.universityGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/university-group/university-groups.html',
                    controller: 'UniversityGroupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('universityGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('university-group-detail', {
            parent: 'entity',
            url: '/university-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.universityGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/university-group/university-group-detail.html',
                    controller: 'UniversityGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('universityGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UniversityGroup', function($stateParams, UniversityGroup) {
                    return UniversityGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'university-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('university-group-detail.edit', {
            parent: 'university-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-group/university-group-dialog.html',
                    controller: 'UniversityGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UniversityGroup', function(UniversityGroup) {
                            return UniversityGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('university-group.new', {
            parent: 'university-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-group/university-group-dialog.html',
                    controller: 'UniversityGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                semester: null,
                                specialisation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('university-group', null, { reload: 'university-group' });
                }, function() {
                    $state.go('university-group');
                });
            }]
        })
        .state('university-group.edit', {
            parent: 'university-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-group/university-group-dialog.html',
                    controller: 'UniversityGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UniversityGroup', function(UniversityGroup) {
                            return UniversityGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('university-group', null, { reload: 'university-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('university-group.delete', {
            parent: 'university-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-group/university-group-delete-dialog.html',
                    controller: 'UniversityGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UniversityGroup', function(UniversityGroup) {
                            return UniversityGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('university-group', null, { reload: 'university-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
