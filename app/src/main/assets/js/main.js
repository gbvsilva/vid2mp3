/* Getting value from Android */
function setVideoSrc(src) {
	var videoDiv = document.getElementById('videoDiv');
	videoDiv.innerHTML = '<video controls><source src=\"'+src+'\" type="video/mp4"></video>';
}