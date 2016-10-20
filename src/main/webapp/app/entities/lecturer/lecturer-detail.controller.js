(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('LecturerDetailController', LecturerDetailController);

    LecturerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lecturer', 'UniversityClass'];

    function LecturerDetailController($scope, $rootScope, $stateParams, previousState, entity, Lecturer, UniversityClass) {
        var vm = this;

        vm.lecturer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:lecturerUpdate', function(event, result) {
            vm.lecturer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
