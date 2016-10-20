(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('ClassroomDetailController', ClassroomDetailController);

    ClassroomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Classroom', 'UniversityClass', 'Building'];

    function ClassroomDetailController($scope, $rootScope, $stateParams, previousState, entity, Classroom, UniversityClass, Building) {
        var vm = this;

        vm.classroom = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:classroomUpdate', function(event, result) {
            vm.classroom = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
