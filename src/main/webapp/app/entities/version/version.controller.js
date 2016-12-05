(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('VersionController', VersionController);

    VersionController.$inject = ['$scope', '$state', 'Version'];

    function VersionController ($scope, $state, Version) {
        var vm = this;
        
        vm.versions = [];

        loadAll();

        function loadAll() {
            Version.query(function(result) {
                vm.versions = result;
            });
        }
    }
})();
