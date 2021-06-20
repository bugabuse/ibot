
var app = angular.module('runeMarketApp', [])
    .config(['$httpProvider', function($httpProvider) {
            $httpProvider.defaults.withCredentials = true;
            $httpProvider.defaults.useXDomain = true;
        }]
    );


var scope;
var http;


app.filter("sanitize", ['$sce', function($sce) {
    return function(htmlCode){
        return $sce.trustAsHtml(htmlCode);
    }
}]);

app.controller('mainController', function($scope, $http) {
    scope = $scope;
    http = $http;
    scope.sendCommand = sendCommand;
    scope.stopAll = stopAll;
    scope.startAll = startAll;
    scope.getData = getData;
    scope.startData = ["test"];
    scope.stateData =  ["test"];
    scope.hostData =  ["test"];
    updateData();
});


updateData();

function startUpdatingData() {
    window.setInterval(updateData, 3000);
}

function updateData() {
    getBotData();
}



function getBotData() {
    scope.botData = {};
      http.get('/manager/list')
          .success(function(data) {
              scope.botData = data;
          });
}


function sendCommand(botAddress, command) {
    http.get('http://' + botAddress + ':6666/?command=' + command)
        .success(function(data) {

        });

}


function getData(botAddress) {
    scope.startData[botAddress] = {};
     http.get('http://' + botAddress + ':6666/?get=data')
         .success(function(data) {
           scope.startData[botAddress] = data.defaultStartData;
           scope.stateData[botAddress] = data.botActive;
           scope.hostData[botAddress] = data.hostName;
         });
}



function startAll() {
    for(var j = 0; j < scope.botData.length; j++) {
        sendCommand(scope.botData[j].ipAddress, "start " + scope.startData[scope.botData[j].ipAddress]);
    }
}
function stopAll() {
    console.log("close all");
    for(var j = 0; j < scope.botData.length; j++) {
        sendCommand(scope.botData[j].ipAddress, "stop");
    }
}