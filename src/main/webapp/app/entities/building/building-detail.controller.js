(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('BuildingDetailController', BuildingDetailController);

    BuildingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Building', 'Classroom'];

    function BuildingDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Building, Classroom) {
        var vm = this;

        vm.building = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('navAppApp:buildingUpdate', function(event, result) {
            vm.building = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
