(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sublocation', {
            parent: 'entity',
            url: '/sublocation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.sublocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sublocation/sublocations.html',
                    controller: 'SublocationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sublocation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sublocation-detail', {
            parent: 'entity',
            url: '/sublocation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.sublocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sublocation/sublocation-detail.html',
                    controller: 'SublocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sublocation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sublocation', function($stateParams, Sublocation) {
                    return Sublocation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sublocation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sublocation-detail.edit', {
            parent: 'sublocation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sublocation/sublocation-dialog.html',
                    controller: 'SublocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sublocation', function(Sublocation) {
                            return Sublocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sublocation.new', {
            parent: 'sublocation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sublocation/sublocation-dialog.html',
                    controller: 'SublocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sublocation', null, { reload: 'sublocation' });
                }, function() {
                    $state.go('sublocation');
                });
            }]
        })
        .state('sublocation.edit', {
            parent: 'sublocation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sublocation/sublocation-dialog.html',
                    controller: 'SublocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sublocation', function(Sublocation) {
                            return Sublocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sublocation', null, { reload: 'sublocation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sublocation.delete', {
            parent: 'sublocation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sublocation/sublocation-delete-dialog.html',
                    controller: 'SublocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sublocation', function(Sublocation) {
                            return Sublocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sublocation', null, { reload: 'sublocation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
