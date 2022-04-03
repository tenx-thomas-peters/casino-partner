(function( $ ) {
	'use strict';
	
    $(document).on('click', '.inquiry', function () {
        let regWindow = window.open('./inquiry', 'inquiry', 'width='+screen.availWidth / 2+',height=600');
        regWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
}).apply( this, [ jQuery ]);

function allCheck(){
	var allCheck = $("#allCheck")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = allCheck;
	}
};

function batchDelete() {
	var checkArray = document.getElementsByClassName("checkboxes");
	var ids = "";
	for(var i = 0; i < checkArray.length; i++){
		if(checkArray[i].checked) {
		    ids += checkArray[i].name + ",";
		}
	}
	if(ids == "") {
		new PNotify({
            title: 'Faild!',
            text: 'Please Check it.',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "note/batchDelete",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				for(var i = 0; i < checkArray.length; i++){
					if(checkArray[i].checked) {
						checkArray[i].checked = false;
					}
				}
				new PNotify({
	                title: 'Success!',
	                text: response.message,
	                type: 'success',
	                buttons: {
	                    closer: true,
	                    sticker: false
	                }
	            });

	            setTimeout(function() {
	                window.location.reload();
	            }, 1000);
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
};

$(document).ready(function() {
	$("#logout-btn").click(function () {
		$.ajax({
			url: CONTEXT_ROOT + 'sys/logout',
			type: 'POST',
			success: function(res) {
				if (res.res == "success") {
					new PNotify({
                        title: 'Success!',
                        text: res.msg,
                        type: 'success',
                        buttons: {
                            closer: true,
                            sticker: false
                        }
                    });
					setTimeout(function() {
                    	window.location.href = CONTEXT_ROOT;
                    }, 1000);
				}
			},
			error: function(err) {
				console.log(err);
				new PNotify({
					title: 'Error!',
                    text: 'Logout Failed!',
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            }
		})	
	})
});

$(document).on('click', '.changePwd-popup', function (e) {
    e.preventDefault();
    let betWindow = window.open(CONTEXT_ROOT + 'member/changePwd', 'Change Password', 'width=1000, height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});