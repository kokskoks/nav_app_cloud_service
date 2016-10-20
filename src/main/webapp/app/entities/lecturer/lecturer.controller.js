(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('LecturerController', LecturerController);

    LecturerController.$inject = ['$scope', '$state', 'Lecturer'];

    function LecturerController ($scope, $state, Lecturer) {
        var vm = this;
        
        vm.lecturers = [];

        loadAll();

        function loadAll() {
            Lecturer.query(function(result) {
                vm.lecturers = result;
            });
        }
    }
})();
