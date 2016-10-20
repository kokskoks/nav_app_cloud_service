(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityGroupController', UniversityGroupController);

    UniversityGroupController.$inject = ['$scope', '$state', 'UniversityGroup'];

    function UniversityGroupController ($scope, $state, UniversityGroup) {
        var vm = this;
        
        vm.universityGroups = [];

        loadAll();

        function loadAll() {
            UniversityGroup.query(function(result) {
                vm.universityGroups = result;
            });
        }
    }
})();
