(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UploadController', UploadController);

    UploadController.$inject = ['$scope','Upload','$log', 'AlertService'];

    function UploadController ($scope, Upload, $log, AlertService) {
        var vm = this;
        
        vm.depts = ['weeia'];
        vm.selectedDept = vm.depts[0];
        
        vm.submit = function(){
        	$log.log("sumbitting...");
        	$log.log(vm.file);
        	
        	if(vm.file != null || vm.file != undefined){
        		Upload.upload({
                    url: 'upload/plan/' + vm.selectedDept,
                    data: {plan: vm.file}
                }).then(function (resp) {
                    console.log('Success ' + resp + 'uploaded. Response: ' + resp.data);
                    
                    AlertService.success("global.alert.upload.success");
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                    AlertService.error("global.alert.upload.error");
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '% ' + evt);
                });
        	}
        };

       
    }
})();
