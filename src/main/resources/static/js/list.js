$(document).ready(
    function () {
        $("#list").click(function() {
            $.ajax({
                type: "GET",
                url: "/list",
                success: function (respuesta, res2) {
                    var tabla = "<table id='result' align='center' border='1' WIDTH='1000' HEIGHT='100'> <tr><th>SHORTENER URL</th><th>ORIGINAL URL</th> <th>Clicks</th> <th>QR code</th></tr>";
                    for (var i=0; i<respuesta.length;i++){
                        tabla += "<tr><td>";
                        tabla += "<a href='" + respuesta[i].hash + "'>" + respuesta[i].hash;
                        tabla += "</td><td>";
                        tabla += "<a href='" + respuesta[i].target + "'>" + respuesta[i].target;
                        tabla += "</td><td>";
                        tabla +=  respuesta[i].clicks;
                        tabla += "</td><td>";
                        tabla += "<input type='button' onclick='generarQr(\"" + respuesta[i].hash + "\")'>GenerarQR</input>";
                        //tabla +=  "<img src='data:image/jpg;base64, " + respuesta[i].qr + "'>";
                        tabla += "</td></tr>";
                    }
                    tabla += "</table>"
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

function generarQr(hash) {
    console.log(hash);
    $.ajax({
        type: "GET",
        url: "/qr",
        data: { "hash" : hash },
        success: function (respuesta) {
            console.log("QR al pulsar " + respuesta.qr);

            //Open a new window with QR code
            var image = new Image();
            image.src = "data:image/jpg;base64," + respuesta.qr;
            var w = window.open('about:blank', 'popup', 'width=250px,height=250px');
            w.document.write(image.outerHTML);

            //$("#qr").html("<img src='data:image/jpg;base64, " + respuesta.qr + "'>");
        },
        error: function () {
            $("#qr").html(
                "<tr><th>QR not generated</th></tr>");
        }
    })
}