(function() {
    'use strict';

    angular
        .module('navAppApp')
        .controller('UploadController', UploadController);

    UploadController.$inject = ['$scope','Upload','$log'];

    function UploadController ($scope, Upload, $log) {
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
                    console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                });
        	}
        };

       
    }
})();
