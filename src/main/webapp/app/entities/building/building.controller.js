(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('BuildingController', BuildingController);

    BuildingController.$inject = ['$scope', '$state', 'DataUtils', 'Building'];

    function BuildingController ($scope, $state, DataUtils, Building) {
        var vm = this;
        
        vm.buildings = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Building.query(function(result) {
                vm.buildings = result;
            });
        }
    }
})();
