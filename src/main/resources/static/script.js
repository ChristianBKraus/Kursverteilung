var app = angular.module('Kursverteilung', []);

app.controller('Ctrl', function($scope, $http) {
			 $scope.actions = [];
			 $scope.students = [];
			 $scope.courses = [];
			 $scope.sameCourses = [];
			 $scope.fixedCourses = [];
			 
			 $scope.readActions = function() {
		     $http.get('api/actions' )
				  .then(  function(response) {
					console.log("Reading Actions");
				    $scope.actions = response.data;
					console.log("response: ");
					$scope.actions.forEach(function(el){
						console.log(el);
					});
				  });
			 }
			 $scope.readStudents = function() {
		     $http.get('api/students' )
			  .then(  function(response) {
				console.log("Reading Students");
			    $scope.students = response.data;
				console.log("response: ");
				$scope.students.forEach(function(el){
					console.log(el);
				});
			  });
			 };
	     		
			 $scope.readCourses = function() {
		     $http.get('api/courses' )
			  .then(  function(response) {
				console.log("Reading Courses");
			    $scope.courses = response.data;
				console.log("response: ");
				$scope.courses.forEach(function(el){
					console.log(el);
				});
			  });
			 };
	     		
			 $scope.readFixedCourses = function() {
		     $http.get('api/fixedCourses' )
			  .then(  function(response) {
				console.log("Reading FixedCourses");
			    $scope.fixedCourses = response.data;
				console.log("response: ");
				$scope.fixedCourses.forEach(function(el){
					console.log(el);
				});
			  });
			 };
	     		
			 $scope.readSameCourses = function() {
		     $http.get('api/sameCourses' )
			  .then(  function(response) {
				console.log("Reading sameCourses");
			    $scope.sameCourses = response.data;
				console.log("response: ");
				$scope.sameCourses.forEach(function(el){
					console.log(el);
				});
			  });
			 };
			 $scope.read = function() {
				 $scope.readActions();
				 $scope.readStudents();
				 $scope.readCourses();
				 $scope.readFixedCourses();
				 $scope.readSameCourses();
			 }
	     		   
		     $scope.read();

		     $scope.optimize = function() {
		    	 console.log("optimize");
		    	 $http.put('api/optimize')
		    	   .then( function success(response) {
		    		  console.log("optimized");
		    		   $scope.read();
		    		   }, function error(response) {
		    	      console.log("error");
		    	      response.data.errors.forEach(function(e) {
		    	    	  console.log(e);
		    	      })

		    	   });
		     }
});		
