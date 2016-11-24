(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('BuildingDetailController', BuildingDetailController);

    BuildingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Building', 'Sublocation', 'Classroom'];

    function BuildingDetailController($scope, $rootScope, $stateParams, previousState, entity, Building, Sublocation, Classroom) {
        var vm = this;

        vm.building = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:buildingUpdate', function(event, result) {
            vm.building = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
