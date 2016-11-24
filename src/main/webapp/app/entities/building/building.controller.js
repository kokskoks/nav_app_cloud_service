(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('BuildingController', BuildingController);

    BuildingController.$inject = ['$scope', '$state', 'Building'];

    function BuildingController ($scope, $state, Building) {
        var vm = this;
        
        vm.buildings = [];

        loadAll();

        function loadAll() {
            Building.query(function(result) {
                vm.buildings = result;
            });
        }
    }
})();
