(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('LecturerDeleteController',LecturerDeleteController);

    LecturerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lecturer'];

    function LecturerDeleteController($uibModalInstance, entity, Lecturer) {
        var vm = this;

        vm.lecturer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lecturer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
