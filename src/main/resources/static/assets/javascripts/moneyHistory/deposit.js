function setRequestAmount(amount) {
	var requestAmount = $('#requestAmount').val();
	if(requestAmount == "") {
		requestAmount = 0;
	} else {
		requestAmount = parseInt($('#requestAmount').val());
	}
	$('#requestAmount').val(requestAmount + parseInt(amount));
}

function reset() {
	$('#requestAmount').val(0);
}

function accountInquiry() {
	$.ajax({
        url: "accountInquiry",
        type: 'GET',
        success: function(res) {
            if (res.success) {
                new PNotify({
                    title: 'Success!',
                    text: res.message,
                    type: 'success',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });

            } else {
                new PNotify({
                    title: 'Error!',
                    text: res.message,
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            }
        },
        error: function(err) {
            new PNotify({
                title: 'Error!',
                text: err.message,
                type: 'error',
                buttons: {
                    closer: true,
                    sticker: false
                }
            });
        }
    });
}

function fast() {
	var receiverSeq = $('#receiver').val();
	var amount = $('#requestAmount').val();
	if(amount == "") {
		new PNotify({
            title: 'Error',
            text: 'Please input amount',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
        });
	} else {
		$.ajax({
	        url: "fast",
	        type: 'POST',
	        data: {receiverSeq: receiverSeq, amount: amount},
	        dataType: 'json',
	        success: function(res) {
	            if (res.success) {
	                new PNotify({
	                    title: 'Success!',
	                    text: res.message,
	                    type: 'success',
	                    buttons: {
	                        closer: true,
	                        sticker: false
	                    }
	                });
	                reset();
	                setTimeout(function() {
	                    window.location.reload();
	                }, 1000);
	            } else {
	                new PNotify({
	                    title: 'Error!',
	                    text: res.message,
	                    type: 'error',
	                    buttons: {
	                        closer: true,
	                        sticker: false
	                    }
	                });
	            }
	        },
	        error: function(err) {
	            new PNotify({
	                title: 'Error!',
	                text: err.message,
	                type: 'error',
	                buttons: {
	                    closer: true,
	                    sticker: false
	                }
	            });
	        }
	    });
	}
}