(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('PlanUploadController', UploadController);

    UploadController.$inject = ['$scope','Upload','$log', 'AlertService'];

    function UploadController ($scope, Upload, $log, AlertService) {
        var vm = this;
        
        vm.depts = ['weeia'];
        vm.selectedDept = vm.depts[0];
        vm.progressPercentage = 0;
        vm.max = 100;
        vm.inProgress = false;
        
        var reset = function(){
        	vm.inProgress = false;
        	vm.progressPercentage = 0;
        	vm.file = null;
        };
        
        
        vm.submit = function(){
        	$log.log("sumbitting...");
        	$log.log(vm.file);
        	
        	if(vm.file != null || vm.file != undefined){
        		vm.inProgress = true;
        		Upload.upload({
                    url: 'upload/plan/' + vm.selectedDept,
                    data: {plan: vm.file}
                }).then(function (resp) {
                    console.log('Success ' + resp + 'uploaded. Response: ' + resp.data);
                    
                    AlertService.success("global.alert.upload.success");
                    reset();
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                    AlertService.error("global.alert.upload.error");
                    reset();
                }, function (evt) {
                    vm.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    
                    console.log('progress: ' + vm.progressPercentage + '% ' + evt);
                });
        	}
        };

       
    }
})();
