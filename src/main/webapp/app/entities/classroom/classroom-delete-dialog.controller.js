(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('ClassroomDeleteController',ClassroomDeleteController);

    ClassroomDeleteController.$inject = ['$uibModalInstance', 'entity', 'Classroom'];

    function ClassroomDeleteController($uibModalInstance, entity, Classroom) {
        var vm = this;

        vm.classroom = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Classroom.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
