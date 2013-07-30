// Lightbox
;(function() {


    $(document).ready(function () { 
      $('.lightboxed').css('cursor', 'pointer');
      $('.lightboxed').click(function (e) { 

        e.preventDefault();
        var image_href = $(this).attr("src");
        if ($('#lightbox').length > 0) { 
            $('#content').html('<img src="' + image_href + '" />');
            //show lightbox window - you could use .show('fast') for a transition
            $('#lightbox').show();
        }
        else {
            var lightbox =
            '<div id="lightbox">' +
                '<p></p>' +
                '<div id="content">' + //insert clicked link's href into img src
                    '<img src="' + image_href +'" />' +
                '</div>' +
            '</div>';
            $('body').append(lightbox);
        }


      });
      $('#lightbox').live('click', function() { 
        $('#lightbox').hide();
      });
      $(document).keyup(function(e) {
        $('#lightbox').hide();
      });
    });

}());
