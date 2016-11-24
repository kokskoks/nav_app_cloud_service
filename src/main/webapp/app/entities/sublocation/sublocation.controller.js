(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('SublocationController', SublocationController);

    SublocationController.$inject = ['$scope', '$state', 'Sublocation'];

    function SublocationController ($scope, $state, Sublocation) {
        var vm = this;
        
        vm.sublocations = [];

        loadAll();

        function loadAll() {
            Sublocation.query(function(result) {
                vm.sublocations = result;
            });
        }
    }
})();
