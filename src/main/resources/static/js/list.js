$(document).ready(
    function () {
        $("#list").click(function() {
            $.ajax({
                type: "GET",
                url: "/list",
                success: function (respuesta) {
                    var tabla = "<tr><th>URI</th><th>URI Acortada</th></tr>";
                    for (var i=0; i<respuesta.length;i++){
                        tabla += "<tr><td>";
                        tabla += respuesta[i].target;
                        tabla += "</td><td>";
                        tabla += respuesta[i].uri;      // ES NULL SIEMPRE
                        tabla += "</td></tr>";
                    }
                    document.getElementById("result").innerHTML = tabla;
                },
                error: function () {
                    $("#result").html(
                        "<tr><th>ERROR</th></tr>");
                }
            });
        });
    }
)