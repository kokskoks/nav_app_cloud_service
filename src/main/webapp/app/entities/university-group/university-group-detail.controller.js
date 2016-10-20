(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityGroupDetailController', UniversityGroupDetailController);

    UniversityGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UniversityGroup', 'UniversityClass'];

    function UniversityGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, UniversityGroup, UniversityClass) {
        var vm = this;

        vm.universityGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:universityGroupUpdate', function(event, result) {
            vm.universityGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
