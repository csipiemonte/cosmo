$(document).ready(function(){
    $('.navbar-toggler').click(function(){
        $(this).toggleClass('is-active');
    });
    $('.it-date-datepicker').datepicker({
        inline:'date-inline'
    });
    $('#cell18-date1').addClass('highlight');
    $('#cell19-date1').addClass('highlight');


    $('.toggle-read').on('click', function(){
        $(this).parent().toggleClass('unread');
        $('i', $(this)).toggleClass('fas').toggleClass('far');
    });
});