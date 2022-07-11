var highlightPhaseDelta = -1;         // -1 reflects no active highlight
var highlightStartPhase = -1;         // -1 reflects no active highlight
var highlightEndPhase = -1;         // -1 reflects no active highlight

var originalZoom = 13.0;

let highlightStart;
var highlightAnimationDuration = 5000;

var highlightCurStartIndex = 100;
var highlightCurEndIndex = 300;

function highlightCurLength() {
    return highlightCurEndIndex - highlightCurStartIndex;
}

var highlightRequestAnimationId = -1;

function highlightReset() {
    window.cancelAnimationFrame(highlightRequestAnimationId);
    highlightStart = null;
    lastPausedAnimationPhase = (highlightCurEndIndex * 1.0) / numCoordinates;

    highlightPhaseDelta = -1;
    highlightStartPhase = -1;
    highlightEndPhase = -1;

    el.style.height = "20px";
    el.style.width = "20px";

    map.once('moveend', function(e){
         finishHighlightEvent();
    });
    map.easeTo({center: coordinatesStream[highlightCurEndIndex], zoom: originalZoom, duration: 500});
}

function highlightPause() {
    isAnimationPlaying = false;
    window.cancelAnimationFrame(highlightRequestAnimationId);
}

function highlightResume() {
    if (highlightPhaseDelta != -1) {
        highlightStart = null;
        isAnimationPlaying = true;
        highlightAnimateBearing();
        highlightRequestAnimationId = window.requestAnimationFrame(frameFocused);
    }
}

function highlightAnimateBearing() {

  let highlightCounter = getCurIndexFromAnimationPhase();

  const highlightStart = coordinatesStream[highlightCounter >= numCoordinates ? highlightCounter - 1 : highlightCounter];
  const highlightEnd = coordinatesStream[highlightCounter >= numCoordinates ? highlightCounter : highlightCounter + 1];

  if (!highlightStart || !highlightEnd) return;

  curPositionBeetle.setRotation(turf.bearing(turf.point(highlightStart), turf.point(highlightEnd)) + 90);

  setTimeout(() => {
    if (isPlayerStatusPlaying()) {
      highlightAnimateBearing();
    }
  }, 100);
}

function initHighlight(highlightStartIndex, highlightEndIndex) {

    isAnimationPlaying = true;
    curPositionBeetle.setLngLat(coordinatesStream[highlightStartIndex]).addTo(map);
    playHighlightEvent();

    highlightStartPhase = (highlightStartIndex * 1.0) / numCoordinates;
    highlightEndPhase = (highlightEndIndex * 1.0) / numCoordinates;
    highlightPhaseDelta = (highlightEndIndex - highlightStartIndex) / numCoordinates;

    el.style.height = "40px";
    el.style.width = "40px";
    fitToCoordinates(coordinatesStream.slice(highlightStartIndex, highlightEndIndex));

    highlightCurStartIndex = highlightStartIndex;
    highlightCurEndIndex = highlightEndIndex;

    highlightAnimateBearing();

    setTimeout(function() {
        highlightRequestAnimationId = window.requestAnimationFrame(frameFocused);
     },100);

}


// when highlight starts, phase = x, where x was the phase of non-highlight portion, calculate
// phase of end of highlight, call it y
// then it needs to increment until y, but in an interval of highlightAnimationDuration
function frameFocused(highlightTime) {

  if (!highlightStart) {
    highlightStart = highlightTime;
  }

  var proportionalTime = (highlightTime - highlightStart) / highlightAnimationDuration;

  animationPhase = lastPausedAnimationPhase +  (proportionalTime * highlightPhaseDelta);

  var highlightCurIndex = getCurIndexFromAnimationPhase();

    if (curPhotoEventIndex < numPhotos && highlightCurIndex >= photosIndexesSorted[curPhotoEventIndex]) {
            playPhotoEvent(photosIndexesSorted[curPhotoEventIndex++]);
            return;
      }

    //TODO this will have same format as the photos
     if (videoIndex != -1 && highlightCurIndex >= videoIndex) {
          videoIndex = -1;
          playVideoEvent();
          return;
     }

  updateAnimationPhase(animationPhase);

  if (animationPhase > highlightEndPhase) {
    highlightReset();
    return
  };


  curPositionBeetle.setLngLat(coordinatesStream[highlightCurIndex]).addTo(map);

  highlightRequestAnimationId = window.requestAnimationFrame(frameFocused);
}
