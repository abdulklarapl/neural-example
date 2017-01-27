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
});

$("#form-train").on("submit", function () {
    var data = {
        expected_output: $(this).find("#train-number").val(),
        payload : getInput($(this).find(".draw-canvas"))
    };
    $.ajax({
        method: "POST",
        url: "/api/digit/train",
        contentType : "application/json",
        data: JSON.stringify(data)
    }).done(function(data){
        console.log(data);
    });
    return false;
});

$("#form-recognize").on("submit", function() {
    var data = {
        payload: getInput($(this).find(".draw-canvas"))
    }
    $.ajax({
        method: "POST",
        url: "/api/digit/recognize",
        contentType: "application/json",
        data: JSON.stringify(data)
    }).done(function(data) {
        $("recognize-output").removeClass("hide").addClass("show").append(data);
    });
    return false;
});

getInput = function(container) {
    var data = "";
    container.find(".cell").each(function() {
        if ($(this).hasClass("fill")) {
            data += "1";
        } else {
            data += "0";
        }
    });

    return data;
};