(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('week', {
            parent: 'entity',
            url: '/week',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.week.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week/weeks.html',
                    controller: 'WeekController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('week');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('week-detail', {
            parent: 'entity',
            url: '/week/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.week.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week/week-detail.html',
                    controller: 'WeekDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('week');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Week', function($stateParams, Week) {
                    return Week.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'week',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('week-detail.edit', {
            parent: 'week-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/week-dialog.html',
                    controller: 'WeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('week.new', {
            parent: 'week',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/week-dialog.html',
                    controller: 'WeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('week', null, { reload: 'week' });
                }, function() {
                    $state.go('week');
                });
            }]
        })
        .state('week.edit', {
            parent: 'week',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/week-dialog.html',
                    controller: 'WeekDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('week', null, { reload: 'week' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('week.delete', {
            parent: 'week',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week/week-delete-dialog.html',
                    controller: 'WeekDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Week', function(Week) {
                            return Week.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('week', null, { reload: 'week' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
