(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityGroupDeleteController',UniversityGroupDeleteController);

    UniversityGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'UniversityGroup'];

    function UniversityGroupDeleteController($uibModalInstance, entity, UniversityGroup) {
        var vm = this;

        vm.universityGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UniversityGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
