(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('SublocationDeleteController',SublocationDeleteController);

    SublocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sublocation'];

    function SublocationDeleteController($uibModalInstance, entity, Sublocation) {
        var vm = this;

        vm.sublocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sublocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
