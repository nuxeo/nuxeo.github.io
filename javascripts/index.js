function index(page, target) {
    var div = $('<div/>');
    div.load(page + " h2,h3", function(response, status, xhr) {
        // console.log(response)
        if (status == "error") {
            var msg = "Sorry but there was an error: ";
            console.error(msg + xhr.status + " " + xhr.statusText);
        } else {
            var h3 = false;
            var items = "";
            $(response).find("h2,h3").each(function(i, item) {
                if ($(item).prop('tagName') === "H3") {
                    if (!h3) {
                        items += '<ul id="' + i + '">';
                        h3 = true;
                    }
                } else if (h3) {
                    items += '</ul>';
                    h3 = false;
                }
                items += '<li><a href="' + page + '#anchor-' + i + '">' + $(item).text() + '</a></li>';
            });
            $(target).html(items);
        }
    });
}
