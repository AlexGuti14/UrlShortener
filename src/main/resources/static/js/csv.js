$(document).ready(function (){
  $(".error").hide();
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
