$(document).ready(function(){
	
	setHeaderInfo();
	setMenuList();

	window.setInterval(setHeaderInfo,8000);
	
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
					alert("새 쪽지 "+res.noteCnt +"개 도착하였습니다");
				}
				$('#holdingMoney').html(res.holdingMoney + " 韩元");
				$('#sameDayFee').html(res.sameDayFee);
				$('#feeCalculator').html(res.feeCalculator);
				let singleLineNotice = "通知: " + res.singleLineNotice; 
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