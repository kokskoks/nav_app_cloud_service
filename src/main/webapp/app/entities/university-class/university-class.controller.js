(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UniversityClassController', UniversityClassController);

    UniversityClassController.$inject = ['$scope', '$state', 'UniversityClass'];

    function UniversityClassController ($scope, $state, UniversityClass) {
        var vm = this;
        
        vm.universityClasses = [];

        loadAll();

        function loadAll() {
            UniversityClass.query(function(result) {
                vm.universityClasses = result;
            });
        }
    }
})();
