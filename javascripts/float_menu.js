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
        var t = jQuery("#welcome").offset().top + 21;
        $mainContainer.animate({top:t},{duration:0,queue:false}); 
        $(window).scroll(function () {
           if ($(document).scrollTop() > t) {
             var offset = $(document).scrollTop()+"px";  
             $mainContainer.animate({top:offset},{duration:0,queue:false});  
           } else {
             $mainContainer.animate({top:t},{duration:0,queue:false});  
           }
        }); 
        $('body').append($mainContainer);
    });

}());
