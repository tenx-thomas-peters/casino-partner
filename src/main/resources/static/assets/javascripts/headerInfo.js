$(document).ready(function(){
	
	setHeaderInfo();
	setMenuList();
	
	function setHeaderInfo() {
		$.ajax({
			url: CONTEXT_ROOT + 'dashboard/getHeaderInfo',
			type: 'POST',
			success: function(res) {
				$('#slotRate').html(res.slotRate + "%");
				$('#casinoRate').html(res.casinoRate + "%");
				$('#noteCnt').html(res.noteCnt);
				if(res.noteCnt > 0) {
					var audio = new Audio();
	            	audio.src = CONTEXT_ROOT + "assets/audio/alarm.ogg";
	            	audio.play();
				}
				$('#holdingMoney').html(res.holdingMoney + " won");
				$('#sameDayFee').html(res.sameDayFee);
				$('#feeCalculator').html(res.feeCalculator);
				let singleLineNotice = "Notice: " + res.singleLineNotice; 
				$('#singleLineNotice').html(singleLineNotice);	
			},
			error: function(err) {
				console.log(err);
			}
		})
	};
	
	function setMenuList() {
		$.ajax({
			url: CONTEXT_ROOT + 'dashboard/getUserType',
			type: 'POST',
			success: function(res) {
				let userType = res.userType;
				if(userType == 1) {
					$('#distributorMenu').attr('style', 'display:none');
					$('#storeMenu').attr('style', 'display:none');
				}
				if (userType == 2) {
					$('#distributorMenu').attr('style', 'display:none');
				} 
			},
			error: function(err) {
				console.log(err);
			}
		})
	}
	
	
});