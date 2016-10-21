(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('WeekController', WeekController);

    WeekController.$inject = ['$scope', '$state', 'Week'];

    function WeekController ($scope, $state, Week) {
        var vm = this;
        
        vm.weeks = [];

        loadAll();

        function loadAll() {
            Week.query(function(result) {
                vm.weeks = result;
            });
        }
    }
})();
