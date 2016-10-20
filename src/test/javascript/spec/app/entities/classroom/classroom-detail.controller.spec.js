'use strict';

describe('Controller Tests', function() {

    describe('Classroom Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClassroom, MockUniversityClass, MockBuilding;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClassroom = jasmine.createSpy('MockClassroom');
            MockUniversityClass = jasmine.createSpy('MockUniversityClass');
            MockBuilding = jasmine.createSpy('MockBuilding');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Classroom': MockClassroom,
                'UniversityClass': MockUniversityClass,
                'Building': MockBuilding
            };
            createController = function() {
                $injector.get('$controller')("ClassroomDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'navAppApp:classroomUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
