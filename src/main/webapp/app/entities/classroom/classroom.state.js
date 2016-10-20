(function() {
    'use strict';

    angular
        .module('navAppApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('classroom', {
            parent: 'entity',
            url: '/classroom',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.classroom.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classroom/classrooms.html',
                    controller: 'ClassroomController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classroom');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('classroom-detail', {
            parent: 'entity',
            url: '/classroom/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navAppApp.classroom.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/classroom/classroom-detail.html',
                    controller: 'ClassroomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classroom');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Classroom', function($stateParams, Classroom) {
                    return Classroom.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'classroom',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('classroom-detail.edit', {
            parent: 'classroom-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classroom/classroom-dialog.html',
                    controller: 'ClassroomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classroom', function(Classroom) {
                            return Classroom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classroom.new', {
            parent: 'classroom',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classroom/classroom-dialog.html',
                    controller: 'ClassroomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desription: null,
                                floor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('classroom', null, { reload: 'classroom' });
                }, function() {
                    $state.go('classroom');
                });
            }]
        })
        .state('classroom.edit', {
            parent: 'classroom',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classroom/classroom-dialog.html',
                    controller: 'ClassroomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Classroom', function(Classroom) {
                            return Classroom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classroom', null, { reload: 'classroom' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('classroom.delete', {
            parent: 'classroom',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/classroom/classroom-delete-dialog.html',
                    controller: 'ClassroomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Classroom', function(Classroom) {
                            return Classroom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('classroom', null, { reload: 'classroom' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
