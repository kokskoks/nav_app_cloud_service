(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('SublocationDetailController', SublocationDetailController);

    SublocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sublocation', 'Building'];

    function SublocationDetailController($scope, $rootScope, $stateParams, previousState, entity, Sublocation, Building) {
        var vm = this;

        vm.sublocation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:sublocationUpdate', function(event, result) {
            vm.sublocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
