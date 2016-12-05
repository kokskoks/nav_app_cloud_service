(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('version', {
            parent: 'entity',
            url: '/version',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.version.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/version/versions.html',
                    controller: 'VersionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('version');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('version-detail', {
            parent: 'entity',
            url: '/version/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.version.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/version/version-detail.html',
                    controller: 'VersionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('version');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Version', function($stateParams, Version) {
                    return Version.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'version',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('version-detail.edit', {
            parent: 'version-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/version/version-dialog.html',
                    controller: 'VersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Version', function(Version) {
                            return Version.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('version.new', {
            parent: 'version',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/version/version-dialog.html',
                    controller: 'VersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ver: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('version', null, { reload: 'version' });
                }, function() {
                    $state.go('version');
                });
            }]
        })
        .state('version.edit', {
            parent: 'version',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/version/version-dialog.html',
                    controller: 'VersionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Version', function(Version) {
                            return Version.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('version', null, { reload: 'version' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('version.delete', {
            parent: 'version',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/version/version-delete-dialog.html',
                    controller: 'VersionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Version', function(Version) {
                            return Version.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('version', null, { reload: 'version' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
