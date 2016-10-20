(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityClassDetailController', UniversityClassDetailController);

    UniversityClassDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UniversityClass', 'Lecturer', 'Classroom', 'UniversityGroup'];

    function UniversityClassDetailController($scope, $rootScope, $stateParams, previousState, entity, UniversityClass, Lecturer, Classroom, UniversityGroup) {
        var vm = this;

        vm.universityClass = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:universityClassUpdate', function(event, result) {
            vm.universityClass = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
