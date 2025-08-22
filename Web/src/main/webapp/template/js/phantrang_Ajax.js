$(document).on('click', '.sotrang', function(e) {
    e.preventDefault(); 
    var page = $(this).attr('data-page');
    $.ajax({
        method: 'POST',
        url: 'http://localhost/DACS2/phantrang',
        data: {
            'page': page
        },
        beforeSend: function() {
            $('#dssp').html('<p>Loading...</p>'); 
        }
    })
    .done(function(data) {
        $('#dssp').html(data); 
    })
    .fail(function() {
        console.log('Error');
    });
});