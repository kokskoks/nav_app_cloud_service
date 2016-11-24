(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('SublocationDialogController', SublocationDialogController);

    SublocationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sublocation', 'Building'];

    function SublocationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sublocation, Building) {
        var vm = this;

        vm.sublocation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.buildings = Building.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sublocation.id !== null) {
                Sublocation.update(vm.sublocation, onSaveSuccess, onSaveError);
            } else {
                Sublocation.save(vm.sublocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navAppApp:sublocationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
