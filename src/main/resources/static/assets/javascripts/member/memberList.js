$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let seq = $(this).parents('tr').find('.member-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+seq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.modal-member-add', function () {
    let regWindow = window.open(CONTEXT_ROOT + 'member/add', 'Member Registration', 'width='+screen.availWidth / 2+',height=600');
    regWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.member-detail', function (e) {
    e.preventDefault();
    let memberSeq = $(this).data('key');
    let detailWindow = window.open('./memberDetails?idx=' + memberSeq, 'Member Detail', 'width='+screen.availWidth *2 / 3+',height=600');
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.money-payment', function (e) {
    e.preventDefault();
    let memberSeq = $(this).data('key');
    let detailWindow = window.open('./moneyPayment?idx=' + memberSeq+ '&act=money', 'Money Payment', 'width='+screen.availWidth / 2+',height=600');
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});