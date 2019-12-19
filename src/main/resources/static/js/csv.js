$(document).ready(function (){
  $(viewCSV).click(function(){
    var rd = new FileReader();
    rd.onload = function (e) {

      var rows = e.target.result.split("\n");
      var filteredList = [];
      for (var i = 0; i < rows.length; i++){
        if(rows[i] != ""){
          rows[i] = rows[i].replace(',', '')
          filteredList.push(rows[i]);
        }
      }
        $.ajax({
            type: "POST",
            url: "/csv",
            data: {'linklist':filteredList},
            success: function (generatedList) {
              var saveURL="";
              for (var i = 0; i < generatedList.length; i++){
                if(filteredList.includes(generatedList[i].target)){
                  filteredList.splice(filteredList.indexOf(generatedList[i].target), 1 );
                  saveURL += generatedList[i].target + "<br/>";
                }
              }
              var w = window.open('about:blank', 'popup', 'width=450px,height=450px');
              w.document.write("Created URLs: <br/>" + saveURL);
            },
            error: function () {
              console.log("Something happened D:");
            }
        });
    }
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
