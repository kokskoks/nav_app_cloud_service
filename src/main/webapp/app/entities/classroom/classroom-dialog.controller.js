(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('ClassroomDialogController', ClassroomDialogController);

    ClassroomDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Classroom', 'UniversityClass', 'Building'];

    function ClassroomDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Classroom, UniversityClass, Building) {
        var vm = this;

        vm.classroom = entity;
        vm.clear = clear;
        vm.save = save;
        vm.universityclasses = UniversityClass.query();
        vm.buildings = Building.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classroom.id !== null) {
                Classroom.update(vm.classroom, onSaveSuccess, onSaveError);
            } else {
                Classroom.save(vm.classroom, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navAppApp:classroomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
