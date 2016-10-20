(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityGroupDialogController', UniversityGroupDialogController);

    UniversityGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UniversityGroup', 'UniversityClass'];

    function UniversityGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UniversityGroup, UniversityClass) {
        var vm = this;

        vm.universityGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.universityclasses = UniversityClass.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.universityGroup.id !== null) {
                UniversityGroup.update(vm.universityGroup, onSaveSuccess, onSaveError);
            } else {
                UniversityGroup.save(vm.universityGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navAppApp:universityGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
