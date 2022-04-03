 $(document).ready(function() {
	$('.distributor-add-form').click(function(){
		let regWindow = window.open('./distributorAddForm', 'Distributor Registration', 'width='+screen.availWidth / 2+',height=600');
		regWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
});
 		
$(document).on('click', '.partner-distributor-detail', function (e) {
    e.preventDefault();
    let storeSeq = $(this).data('key');
    let detailWindow = window.open('./memberDetails?idx=' + storeSeq, 'Partner Distributor Detail', 'width='+screen.availWidth *2 / 3+',height=600');
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let memberSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});
	
let today = moment().format('YYYY-MM-DD');

$(document).on('click', '.distributor-storeCount', function (e) {
    e.preventDefault();
    let distributorSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./shopMember?seq=' + distributorSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, 'Sub-Store List', 'width='+screen.availWidth+',height=900');
});

$(document).on('click', '.distributor-memberCount', function (e) {
    e.preventDefault();
    let distributorSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./Member?seq=' + distributorSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, 'Sub-Store List', features);
});

