(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('WeekDetailController', WeekDetailController);

    WeekDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Week'];

    function WeekDetailController($scope, $rootScope, $stateParams, previousState, entity, Week) {
        var vm = this;

        vm.week = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navAppApp:weekUpdate', function(event, result) {
            vm.week = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
