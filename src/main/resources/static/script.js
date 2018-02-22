var app = angular.module('Cockpit', []);

app.controller('Ctrl', function($scope, $http) {
			 $scope.header = { id: 1, year: "2016", period: "009", status: "New" };
			 $scope.activities = [];
		   
		     $http.get('closing/' + $scope.header.id.toString() )
				  .then(  function(response) {
					console.log("Closing read" + $scope.header.id.toString());
					console.log("response: " + response);
				    $scope.header = response.data;
				    console.log("closing = " + $scope.header);
					$scope.getactivities();
				  });
		     		   
		     $scope.getactivities = function() {
				var g_activities = 'closing/' + $scope.header.id.toString() + '/activity';
				console.log( "GET " + g_activities );
				$http.get( g_activities )
					 .then( function(response) {
          			   console.log("Activities read");
          			   $scope.activities = response.data; 
          			   console.log("activities = " + $scope.activities);
          			  });
		     };	
		     
 			 $scope.action = function(event, id) {
 				 var activity = $(event.currentTarget).attr("data-id");
 				 console.log("action (" + activity + "/" + id + ")");
				 var request = 'closing/' + 
				               $scope.header.id.toString() + "/" + 
				               'activity' + "/" + activity;
				 console.log("POST " + request);
				 $http.post(request)
				      .then( function(activity) { console.log("POST COMPLETE" ); }, 
					     	 function(error) { console.log(error); }
				            );	
 			 };
});		
