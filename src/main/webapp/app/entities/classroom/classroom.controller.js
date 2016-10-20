(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('ClassroomController', ClassroomController);

    ClassroomController.$inject = ['$scope', '$state', 'Classroom'];

    function ClassroomController ($scope, $state, Classroom) {
        var vm = this;
        
        vm.classrooms = [];

        loadAll();

        function loadAll() {
            Classroom.query(function(result) {
                vm.classrooms = result;
            });
        }
    }
})();
