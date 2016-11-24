(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('BuildingDialogController', BuildingDialogController);

    BuildingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Building', 'Sublocation', 'Classroom'];

    function BuildingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Building, Sublocation, Classroom) {
        var vm = this;

        vm.building = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sublocations = Sublocation.query();
        vm.classrooms = Classroom.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.building.id !== null) {
                Building.update(vm.building, onSaveSuccess, onSaveError);
            } else {
                Building.save(vm.building, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navAppApp:buildingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
