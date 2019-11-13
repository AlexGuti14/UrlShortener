$(document).ready(function (){
  $(".error").hide();
  $(viewCSV).click(function(){
    console.log();
    var rd = new FileReader();
    rd.onload = function (e) {

      var rows = e.target.result.split("\n");
      var filteredList = [];

      for (var i = 0; i < rows.length; i++){
        if(rows[i] != ""){
          filteredList.push(rows[i]);
        }
      }

      var linklistJSON = JSON.stringify(filteredList);
        $.ajax({
            type: "POST",
            url: "/csv",
            dataType: 'json',
            data: {'linklist': linklistJSON},
            success: function (msg) {
            },
            error: function () {
            }
        });
    }
    var CSVfiledata = $("#inputCSV")[0].files[0];
    if(CSVfiledata.name.includes(".csv")){
      rd.readAsText($("#inputCSV")[0].files[0]);
      $(".error").hide();
    }else{
      $(".error").show();
    }

  });
});
