'use strict';

describe('Controller Tests', function() {

    describe('UniversityClass Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUniversityClass, MockLecturer, MockClassroom, MockUniversityGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUniversityClass = jasmine.createSpy('MockUniversityClass');
            MockLecturer = jasmine.createSpy('MockLecturer');
            MockClassroom = jasmine.createSpy('MockClassroom');
            MockUniversityGroup = jasmine.createSpy('MockUniversityGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UniversityClass': MockUniversityClass,
                'Lecturer': MockLecturer,
                'Classroom': MockClassroom,
                'UniversityGroup': MockUniversityGroup
            };
            createController = function() {
                $injector.get('$controller')("UniversityClassDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'navAppApp:universityClassUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
