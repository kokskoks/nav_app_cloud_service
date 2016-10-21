(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityClassDialogController', UniversityClassDialogController);

    UniversityClassDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UniversityClass', 'Week', 'Lecturer', 'Classroom', 'UniversityGroup'];

    function UniversityClassDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UniversityClass, Week, Lecturer, Classroom, UniversityGroup) {
        var vm = this;

        vm.universityClass = entity;
        vm.clear = clear;
        vm.save = save;
        vm.weeks = Week.query();
        vm.lecturers = Lecturer.query();
        vm.classrooms = Classroom.query();
        vm.universitygroups = UniversityGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.universityClass.id !== null) {
                UniversityClass.update(vm.universityClass, onSaveSuccess, onSaveError);
            } else {
                UniversityClass.save(vm.universityClass, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navAppApp:universityClassUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
