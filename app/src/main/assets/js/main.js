/* Listening status of video loading process */
function toggleElement(element) {
    var docElem = document.getElementById(element);
	if(docElem.classList.contains('hidden')) {
	    docElem.classList.remove('hidden');
	}else {
	    docElem.classList.add('hidden');
	}
}

/* Getting value from Android */
function setVideoSrc(src) {
	var videoDiv = document.getElementById('videoDiv');
	videoDiv.innerHTML = '<video controls><source src=\"'+src+'\" type="video/mp4"></video>';
}