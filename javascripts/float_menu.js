// Floating Menu
;(function() {


    $(document).ready(function () { 
        var  $mainContainer = $("<div id='floatMenu'>");
        var menuYloc;
        $menuContainer = $("<ul>");
        $.each( $("h2"), function (i,item) {
            $menuContainer.append('<li><a href="#anchor-'+i+'">'+$(item).html()+'</a></li>');
            $(item).html('<a name="anchor-'+i+'">'+$(item).html()+'</a>');
        });
        $mainContainer.append($menuContainer);

       
        $(window).scroll(function () {
            if(!menuYloc)
                menuYloc = parseInt($mainContainer.css("top").substring(0,$mainContainer.css("top").indexOf("px")));
            var offset = menuYloc+$(document).scrollTop()+"px";  
            $mainContainer.animate({top:offset},{duration:500,queue:false});  
        }); 
        $('body').append($mainContainer);
    });

}());
