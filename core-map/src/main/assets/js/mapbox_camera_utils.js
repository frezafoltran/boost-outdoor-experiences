const el = document.createElement('div');
el.className = 'marker';

const highlightMarker = new mapboxgl.Marker(el);

const highlightPointLabel = 'highlightPoint';

var highlightPhaseDelta = -1;         // -1 reflects no active highlight
var highlightStartPhase = -1;         // -1 reflects no active highlight
var highlightEndPhase = -1;         // -1 reflects no active highlight

var originalZoom = 13.0;

let highlightStart;
var highlightAnimationDuration = 5000;

var highlightCurStartIndex = 100;
var highlightCurEndIndex = 300;
var isMapAlreadyHighlighting = false;

function highlightCurLength() {
    return highlightCurEndIndex - highlightCurStartIndex;
}

var totalNumberOfIndexes = 1;

var highlightRequestAnimationId = -1;


function highlightReset() {
    window.cancelAnimationFrame(highlightRequestAnimationId);
    highlightStart = null;
    lastPausedPhase = (highlightCurEndIndex * 1.0) / (steps * 1.0);

    highlightPhaseDelta = -1;
    highlightStartPhase = -1;
    highlightEndPhase = -1;

    el.style.height = "20px";
    el.style.width = "20px";

    map.once('moveend', function(e){
            AndroidMapPlayer.removeHighlightEvent();
        });
    map.easeTo({center: routes[highlightCurEndIndex], zoom: originalZoom, duration: 500});
}

function highlightPause() {
    isMapAlreadyHighlighting = false;
    window.cancelAnimationFrame(highlightRequestAnimationId);
}

function highlightResume() {
    if (highlightPhaseDelta != -1) {
        highlightStart = null;
        isMapAlreadyHighlighting = true;
        highlightAnimateBearing();
        highlightRequestAnimationId = window.requestAnimationFrame(frameFocused);
    }
}

function highlightAnimateBearing() {

  let highlightCounter = getCurIndexFromPhase();

  const highlightStart = routes[highlightCounter >= steps ? highlightCounter - 1 : highlightCounter];
  const highlightEnd = targetRoute[highlightCounter >= steps ? highlightCounter : highlightCounter + 1];

  if (!highlightStart || !highlightEnd) return;

  highlightMarker.setRotation(turf.bearing(turf.point(highlightStart), turf.point(highlightEnd)) + 90);

  setTimeout(() => {
    if (AndroidMapPlayer.isMapHighlight()) {
      highlightAnimateBearing();
    }
  }, 100);
}

function initHighlight(highlightStartIndex, highlightEndIndex) {

    isMapAlreadyHighlighting = true;
    highlightMarker.setLngLat(routes[highlightStartIndex]).addTo(map);
    AndroidMapPlayer.sendHighlightEvent();

    highlightStartPhase = (highlightStartIndex * 1.0) / (steps * 1.0);
    highlightEndPhase = (highlightEndIndex * 1.0) / (steps * 1.0);
    highlightPhaseDelta = (highlightEndIndex - highlightStartIndex) / (steps * 1.0);

    el.style.height = "40px";
    el.style.width = "40px";
    fitToCoordinates(routes.slice(highlightStartIndex, highlightEndIndex));

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

  phase = lastPausedPhase +  (proportionalTime * highlightPhaseDelta);

  var highlightCurIndex = getCurIndexFromPhase();

  if (nextPhotoIndexToShow < photosIndexes.length && highlightCurIndex >= photosIndexes[nextPhotoIndexToShow]) {
          AndroidMapPlayer.showPhotoAtIndex(photosIndexes[nextPhotoIndexToShow++])
    }

   if (videoIndex != -1 && highlightCurIndex >= videoIndex) {
               videoIndex = -1;
             AndroidMapPlayer.showVideo();
       }

  AndroidMapPlayer.updateCurPhase(phase);

  if (phase > highlightEndPhase) {
    highlightReset();
    return
  };



  highlightMarker.setLngLat(routes[highlightCurIndex]).addTo(map);

  highlightRequestAnimationId = window.requestAnimationFrame(frameFocused);

}