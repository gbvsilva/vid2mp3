/* Sending value to Android */
function toggleElement(element) {
	var classList = element.classList;
	if(classList.indexOf('hidden') === -1) {
		classList.add('hidden');
	}else {
		classList.remove('hidden');
	}
}

/* Getting value from Android */
function getVideoSrc() {
	var vidDiv = document.getElementById('vidDiv');
	var videoSrc = AndroidInterface.getVideoUrl();
	vidDiv.innerHTML = '<video src=\"'+videoSrc+'\"></video>';
}
