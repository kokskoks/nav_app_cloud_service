(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('VersionDetailController', VersionDetailController);

    VersionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Version'];

    function VersionDetailController($scope, $rootScope, $stateParams, previousState, entity, Version) {
        var vm = this;

        vm.version = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:versionUpdate', function(event, result) {
            vm.version = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
