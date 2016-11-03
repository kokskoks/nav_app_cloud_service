(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('university-class', {
            parent: 'entity',
            url: '/university-class',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.universityClass.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/university-class/university-classes.html',
                    controller: 'UniversityClassController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('universityClass');
                    $translatePartialLoader.addPart('classType');
                    $translatePartialLoader.addPart('weekday');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('university-class-detail', {
            parent: 'entity',
            url: '/university-class/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.universityClass.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/university-class/university-class-detail.html',
                    controller: 'UniversityClassDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('universityClass');
                    $translatePartialLoader.addPart('classType');
                    $translatePartialLoader.addPart('weekday');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UniversityClass', function($stateParams, UniversityClass) {
                    return UniversityClass.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'university-class',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('university-class-detail.edit', {
            parent: 'university-class-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-class/university-class-dialog.html',
                    controller: 'UniversityClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UniversityClass', function(UniversityClass) {
                            return UniversityClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('university-class.new', {
            parent: 'university-class',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-class/university-class-dialog.html',
                    controller: 'UniversityClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                moduleCode: null,
                                description: null,
                                type: null,
                                startHour: null,
                                endHour: null,
                                weekday: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('university-class', null, { reload: 'university-class' });
                }, function() {
                    $state.go('university-class');
                });
            }]
        })
        .state('university-class.edit', {
            parent: 'university-class',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-class/university-class-dialog.html',
                    controller: 'UniversityClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UniversityClass', function(UniversityClass) {
                            return UniversityClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('university-class', null, { reload: 'university-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('university-class.delete', {
            parent: 'university-class',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/university-class/university-class-delete-dialog.html',
                    controller: 'UniversityClassDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UniversityClass', function(UniversityClass) {
                            return UniversityClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('university-class', null, { reload: 'university-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
