$(document).ready(function() {
	
	$(document).on('click', '.partner-store-detail', function (e) {
        e.preventDefault();
        let storeSeq = $(this).data('key');
        detailWindow = window.open('./memberDetailsStore?idx=' + storeSeq, 'Partner Store Detail', 'width='+screen.availWidth *2 / 3+',height=600');
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	let today = moment().format('YYYY-MM-DD');
	
	$(document).on('click', '.progress-bet', function (e) {
		e.preventDefault();
		let distributorSeq = $(this).parents('tr').find('.store-seq').data('key');
		let losingAmount = $('.losingAmount').data('key');
		let betWindow = window.open(CONTEXT_ROOT + 'partner/popup_bet?distributorSeq=' + distributorSeq + '&fromProcessTime=' + today + '&toProcessTime=' + today + '&losingAmount=' + losingAmount, 'Store Summary', 'width='+(screen.availWidth - 100)+', height=600');
	    betWindow.onbeforeunload = function () {
	        window.location.reload();
	    }
	});
	
	$(document).on('click', '.store-memberCount', function (e) {
	    e.preventDefault();
	    let storeSeq = $(this).parents('tr').find('.store-seq').data('key');
	    let detailWindow = window.open('./Member?seq=' + storeSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 1, 'Sub-Store List', features);
	    detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
	$('.store-add-form').click(function(){
		let regWindow = window.open('./storeAddForm', 'Store Registration', 'width='+screen.availWidth / 2+',height=600');
		regWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
});
