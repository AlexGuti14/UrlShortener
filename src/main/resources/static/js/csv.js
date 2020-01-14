$(document).ready(function (){
  $(viewCSV).click(function(){
    var stompClient = null;
    var generatedList = [];
    var count = 1;
    var rd = new FileReader();
    rd.onload = function (e) {

      //Procesado del CSV
      var rows = e.target.result.split("\n");
      var filteredList = [];
      for (var i = 0; i < rows.length; i++){
        if(rows[i] != ""){
          rows[i] = rows[i].replace(',', '')
          filteredList.push(rows[i]);
        }
      }

      //Función que establece la conexión por medio de WebSocket.

      function connect() {
          var socket = new SockJS('/links-websocket');
          stompClient = Stomp.over(socket);
          stompClient.connect({}, function (frame) {
              console.log('Connected: ' + frame);
              stompClient.subscribe("/topic/links", function (item) {
              if(count < limit)
              {
                item = JSON.parse(item.body);
                generatedList.push(item.target);
                count++

              }else
              {
                item = JSON.parse(item.body);
                generatedList.push(item.target);
                disconnect();
                showLinks(generatedList);
              }

            });
            var limit = filteredList.length;
            for(var j = 0; j < filteredList.length; j++){
               stompClient.send("/app/csv", {},filteredList[j]);
            }
          });
      }

      //Función que regresa los URLs acortados.

      function showLinks(generatedList) {
              var saveURL="";
              for (var i = 0; i < generatedList.length; i++){
                if(filteredList.includes(generatedList[i])){
                  filteredList.splice(filteredList.indexOf(generatedList[i]), 1 );
                  saveURL += generatedList[i] + "<br/>";
                }
              }
              var w = window.open('about:blank', 'popup', 'width=450px,height=450px');
              w.document.write("Created URLs: <br/>" + saveURL);
            }

      //Función que cierra la conexión de WebSocket.

      function disconnect() {
          if (stompClient !== null) {
              stompClient.disconnect();
          }
          console.log("Disconnected");
      }
      connect();
    }

    //Procesado y control de los archivos subidos por el usuario.

    var CSVfiledata = $("#inputCSV")[0].files[0];
    if(CSVfiledata != null){
      if(CSVfiledata.name.includes(".csv")){
        rd.readAsText($("#inputCSV")[0].files[0]);
      }else{
        alert("The selected file wasn't a .CSV file.");
      }
    }else{
      alert("Please select a file to load");
    }


  });
});
