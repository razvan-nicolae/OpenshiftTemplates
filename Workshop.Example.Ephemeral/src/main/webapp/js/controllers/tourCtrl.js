'use strict';

todomvc.controller('TourCtrl', function TodoCtrl($scope, $timeout) {
	$scope.publicdomain = "km.orange-labs.fr";
	$scope.consoleHome = "https://master."+$scope.globaldomain;
	$scope.defaultAppName="application_name";
	$scope.appName = $scope.defaultAppName;
	$scope.currentStep = -1;
	$scope.nbSteps = 0;
	$scope.isBonusVisible = false;
	
	$scope.onKonami = function(){
		$(".logo").addClass("konami");
	}

	/*
		Initialize tour with appName and domain extracted form actual URL
	*/
	$scope.init = function(){
		if($scope.appName == $scope.defaultAppName){
			//R�cup�re le domaine ex : atelier-demo.beta.kermit.rd.francetelecom.fr
			var host = window.location.host;
			if( host !== ""){
				var nameAndDomain = host.split('.')[0].split('-');
				$scope.appName = nameAndDomain[0];
				$scope.domain = nameAndDomain[1];
			}
		}
		if(!$scope.appName){
			$scope.appName = $scope.defaultAppName;
		}
		console.log("appname = ",$scope.appName);
		console.log("domain = ",$scope.domain);
	};
	
	/*
		Restart tour to the last known position based on sessionStorage status
	*/
	$scope.restartToLastPosition = function(){
		if(sessionStorage["tour"]){
			
			console.log("starting tour for appname = ",$scope.appName);
			console.log("starting tour for domain = ",$scope.domain);
			var tourStatus = JSON.parse(sessionStorage["tour"]);
			$scope.startTour(tourStatus.id, tourStatus.nbSteps);
			hopscotch.showStep(tourStatus.step);
		}
	};
	
	$scope.appPublicDNS =function(){
		return $scope.appName+"-"+$scope.domain+"."+$scope.publicdomain;
	}
	
	$scope.appHttpUrl = function(){
		return "http://"+$scope.appName+"-"+$scope.domain+"."+$scope.globaldomain;
	};
	
	$scope.newStep = function(tourId, id){
		return {
			title: document.querySelector("[data-tour='"+tourId+"'] [data-tour-step='"+id+"']").title,
			content: document.querySelector("[data-tour='"+tourId+"'] [data-tour-step='"+id+"']").innerHTML,
			target: "header",
			placement: "right",
			showPrevButton : true
		};
	}
	
	$scope.newTour = function(id, nbSteps, onEnd){
		var tour = {
			id: id,
			bubbleWidth : 400,
			bubblePadding : 20,
			i18n : {
				prevBtn:"Prev.",
				nextBtn:"Next",
				doneBtn:"End"
			  },
			steps: []
		};
		
		for(var i = 1; i<=nbSteps; i++){
			tour.steps.push($scope.newStep(id,i));
		};
	
		tour.onShow = [function(){
			sessionStorage["tour"] = JSON.stringify({
				id : id,
				nbSteps : nbSteps,
				step : hopscotch.getCurrStepNum()
			});
			$scope.currentStep =  hopscotch.getCurrStepNum();
			console.log("onPrev or onNext");
			if(!$scope.$$phase) {$scope.$digest()};

		}]
		
		tour.onEnd = tour.onClose = function(){
			delete sessionStorage["tour"];
			$scope.currentStep = -1;
			$scope.nbSteps = 0;
			console.log("onEnd or onClose");
			if(!$scope.$$phase) {$scope.$digest()};
		}
		
		if(id == 'normal'){
			tour.onShow.push(function(){
				if(hopscotch.getCurrStepNum() == (nbSteps-1)){
					console.log("bonus are now visible");
					$scope.isBonusVisible = true;
					if(!$scope.$$phase) {$scope.$digest()};
				};
			});
		};
		
		return tour;
	};
	
	$scope.stepNumbers = function(){
		var range = [];
		for(var i=1;i<=$scope.nbSteps;i++){
			range.push(i);
		}
		return range;
	}
	
	$scope.stepClass = function(step){
		if(hopscotch.getCurrStepNum() >= step){
			return "done";
		}
		if(hopscotch.getCurrStepNum() == step-1){
			return "current";
		}
		return "notdone";
	}
	
	$scope.setStep = function(index){
		hopscotch.showStep(index);
	}
	
	$scope.startTour = function(id, nbSteps){
		hopscotch.endTour();
		$scope.nbSteps = nbSteps;
		hopscotch.startTour($scope.newTour(id,nbSteps));
	};
	
	$scope.init();
	//Render dom before creating tour static step HTML
	$timeout($scope.restartToLastPosition, 10);
	
	$(function(){
		var kKeys = [];
		function Kpress(e){
			kKeys.push(e.keyCode);
			if (kKeys.toString().indexOf("38,38,40,40,37,39,37,39,66,65") >= 0) {
				$(this).unbind('keydown', Kpress);
				$scope.onKonami();
			}
		}
		$(document).keydown(Kpress);
	});
	
});
