$(function() {
    var token = null;
    var userId = null;
    var offset = 0;
    var count = 5;
    var total = 0;
    //alert("Please use this form to login");
    $("#gethouse").hide();
    $("#houseRow").hide();
    $("#signin").click(function (e) {
        e.preventDefault();
        jQuery.ajax ({
            url:  "/api/sessions",
            type: "POST",
            data: JSON.stringify({emailAddress:$("#inputEmail").val(), password: $("#inputPassword").val()}),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function(data){
            $("#greeting").text("Hello " + data.data.firstName);
            $("#gethouse").show();
            $("#houseTable").find(".cloned").remove();
            token = data.data.token;
            userId = data.data.userId;
        })
            .fail(function(data){

                $("#greeting").text("You might want to try it again");
                $("#gethouse").hide();
            })
    });

    $("#gethouse").click(function (e) {
        e.preventDefault();
        loadhouses();
    });

    $("#next").click(function(e){
        e.preventDefault();
        if (offset+count < total) {
            offset = offset+count;
            loadhouses();
        }
    })

    $("#previous").click(function(e){
        e.preventDefault();
        console.log("Cliked")
        if (offset-count >= 0) {
            offset = offset-count;
            loadhouses();

        }
    })
    function loadhouses() {
        jQuery.ajax ({
            url:  "/api/owners/" + userId + "/houses?offset=" + offset + "&count="  + count,
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader ("Authorization", token);
            }
        })
            .done(function(data){
                total = data.metadata.total;
                $("#page").text("Page " + Math.floor(offset/count+1) + " of " + (Math.ceil(total/count)));
                $("#houseTable").find(".cloned").remove();
                data.content.forEach(function(item){
                    $( "#houseRow" ).clone().prop("id",item.id).appendTo( "#houseTable" );
                    $("#"+item.id).find("#state").text(item.state);
                    $("#"+item.id).find("#zipcode").text(item.zipcode);
                    $("#"+item.id).find("#year").text(item.year);
                    $("#"+item.id).prop("class","cloned");
                    $("#"+item.id).show();
                });
            })
            .fail(function(data){
                $("#houselist").text("Sorry no houses");
            })

    }
})


