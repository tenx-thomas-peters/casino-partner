$(document).ready(function(){
	$('#userType').change(function(){
		var level = $("#levelType").val();
		var userType = $("#userType").val();
		getMemberList(level, userType);
	});
	
	$('#levelType').change(function(){
		var level = $("#levelType").val();
		var userType = $("#userType").val();
		getMemberList(level, userType);
	});
	
	$('select[name=levelSeq]').trigger('change');
	
	$(document).on('click', '.send-note', function (e) {
        e.preventDefault();
        if (!$('#noteForm').valid())
            return;

        $('#noteForm').submit();
    });
	
	$("#noteForm").validate({
        submitHandler: function(form) {
            $.ajax({
                url: "send",
                type: 'POST',
                data: $('#noteForm').serialize(),
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
    });
	
	$(document).on('click', '.memo-add', function (e) {
        e.preventDefault();
        if (!$('#pNoteForm').valid())
            return;

        $('#pNoteForm').submit();
    });
	
	$(document).on('click', '.noteTitle', function(e){
		let closed = $(this).attr("data-closed");
		if (closed == 0) {
			$(this).attr("data-closed", 1);
			let title = $(this).text();
			let content = $(this).data("content").replace(',<p>', '').replace('<p>', '');
			 
			let detailTr = $('<tr class="detailTd"></tr>')
							.append($("<td colspan='20'></td>").append($("<div></div>").append("<div class=row></div>")
							.append("<div class='col-md-1' style='float:left!important'>Title : </div>")
							.append("<div class='col-md-10' style='text-align: left'>" + title +"</div class='row'></div><br/><br/>")
							.append("<div class='col-md-1'>Content : </div>").append("<div class='col-md-10' style='text-align: left'>" + content + "</div>")));
			$(this).parent().parent().after(detailTr);

            let note_seq = $(this).data("noteseq");
            // set read status for note
            let readStatus = $(this).data("readstatus");
            console.log(note_seq);
            console.log(readStatus);
            if(readStatus === 0){
                $.ajax({
                    url: "changeReadNote",
                    type: "GET",
                    data: {seq: note_seq},
                    success: function (res){
                        if(res.success){
                            console.log(res);
                            $(".note_read_status").html("<span>읽음</span>");
                        }
                    }
                });
            }
		} else {
			$(this).attr("data-closed", 0);
			$(this).parent().parent().next().remove();
		}	
	});

    $(document).on('click', '.deleteBtn', function(e){
        let seq = $(this).data('seq');
        if (confirm("정말 삭제하시겠습니까?") == true) {
            $.ajax({
                url: "deleteNote",
                type: "GET",
                data: {seq: seq},
                success: function (res){
                    if(res.success){
                        console.log(res);
                        setTimeout(function() {
                            window.location.reload();
                        }, 1000);
                    }
                }
            });
        }
    })

	$("#pNoteForm").validate({
        submitHandler: function(form) {
            $.ajax({
                url: "memoadd",
                type: 'POST',
                data: $('#pNoteForm').serialize(),
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
    });
});

function getMemberList(level, userType) {
	$.ajax({
		url: "getMemberList",
		type: "GET",
		data: { "levelSeq": level, "userType": userType },
		success: function(response){
			$('select[name="mSeq"]').html($('<option value=""></option>'));
			response.map(function(item) {
				$('select[name="mSeq"]').append($('<option value="' + item.seq + '">' + item.nickname + '</option>'));
			})
		}
	});
}

function readAll() {
	$.ajax({
		url: "readAll",
		type: "GET",
		data: {},
		success: function(res) {
			new PNotify({
                title: 'Success!',
                text: res.message,
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
	})
}

function batchDelete() {
	$.ajax({
		url: CONTEXT_ROOT + "note/batchDelete",
		type: "POST",
		data: {},
		success: function(response) {
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