(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityClassDeleteController',UniversityClassDeleteController);

    UniversityClassDeleteController.$inject = ['$uibModalInstance', 'entity', 'UniversityClass'];

    function UniversityClassDeleteController($uibModalInstance, entity, UniversityClass) {
        var vm = this;

        vm.universityClass = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UniversityClass.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
