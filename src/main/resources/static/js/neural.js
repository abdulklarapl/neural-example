$.fn.prepareCanvas = function () {
    html = "";
    for (i = 0; i < 625; i++) {
        html += '<div class="cell" data-data="0" />'
    }

    $(this).append(html);

    $(this).on("click")

    $(document).on("mousemove", ".cell", function (event) {
        if (!event.which) {
            return;
        }
        $(this).addClass("fill").attr("data-data", "1");
    });
};
$(document).ready(function () {
    $(".draw-canvas").prepareCanvas();

    $("#form-digit button[data-action=train]").on("click", function () {
        var data = {
            expected_output: $("#train-number").val(),
            payload: getInput($(".draw-canvas"))
        };
        $.ajax({
            method: "POST",
            url: "/api/digit/train",
            contentType: "application/json",
            data: JSON.stringify(data)
        })
        .done(function (data) {
            $("#training-output").removeClass("show").addClass("hide");
            var container = $("#train-output").removeClass("hide").addClass("show");
            container.find("#recognize-digit").html(data.recognized_int);
        });
        $("#recognize-output").removeClass("show").addClass("hide");
        $("#training-output").removeClass("hide").addClass("show");

        return false;
    });

    $("#form-digit button[data-action=submit]").on("click", function () {
        var data = {
            payload: getInput($(".draw-canvas"))
        }
        $.ajax({
            method: "POST",
            url: "/api/digit/recognize",
            contentType: "application/json",
            data: JSON.stringify(data)
        }).done(function (data) {
            $("#training-output").removeClass("show").addClass("hide");
            $("#train-output").removeClass("show").addClass("hide");
            var container = $("#recognize-output").removeClass("hide").addClass("show");
            container.find("#recognize-digit").html(data.recognized_int);

            var output = [];
            for (var i =0;i<data.output.length;i++) {
                var length = Math.round(data.output[i]*4000);
                output.push(i+" <span class='result' style='width:"+length+"px;'>"+Math.round(data.output[i]*100000)/10000+"</span>");
            }
            container.find("#recognize-calculations").html(output.join("<br/>"));
        });
        return false;
    });

    $("#form-digit button[data-action=visualize]").on("click", function () {
        fillInput($(".draw-canvas"), $("#visualize-string").val());
    });

    $("button[type=reset]").on("click", function() {
        $(this).parent().parent().find(".draw-canvas .cell").removeClass("fill");
    });
});

getInput = function (container) {
    var data = "";
    container.find(".cell").each(function () {
        if ($(this).hasClass("fill")) {
            data += "1";
        } else {
            data += "0";
        }
    });

    return data;
};

fillInput = function (container, data) {
    var index = 0;
    container.find(".cell").each(function() {
        if (data[index] == "1") {
            $(this).addClass("fill");
        } else {
            $(this).removeClass("fill");
        }
        index++;
    })
};