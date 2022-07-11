/*
this file sets up the player and the handlers to communicate with the JS interface
*/

const cameraAltitude = 3000;
const animateBearingInterval = 100;
var mapLoaded = false;

//static data statuses
var curHighlightEventIndex = 0;
var curPhotoEventIndex = 0;
var videoIndex = 300;

var isHighlightEventPlaying = false;

// responsible for animation time updates
let mainAnimationStartTime;
let requestAnimationId;
var animationPhase = 0;
var lastPausedAnimationPhase = 0;
var isAnimationPlaying = false;

function getCurIndexFromAnimationPhase() {
    return parseInt(numCoordinates * animationPhase);
}

function resetAnimation() {
  window.cancelAnimationFrame(requestAnimationId);
  isAnimationPlaying = false;
  isHighlightEventPlaying = false;

  lastPausedAnimationPhase = 0;
  animationPhase = 0;
  mainAnimationStartTime = null;
}

function pauseAnimation() {
    isAnimationPlaying = false;
    lastPausedAnimationPhase = animationPhase;
    window.cancelAnimationFrame(requestAnimationId);
}

function startAnimation() {
     isAnimationPlaying = true;
     mainAnimationStartTime = null;
     animateBearing();
     requestAnimationId = window.requestAnimationFrame(mainAnimationFrame);
}

function prepareForHighlight(startIndexOfHighlightEvent) {
    //makes sure that when highlight finishes, phase updates correctly
    lastPausedAnimationPhase = (startIndexOfHighlightEvent * 1.0) / numCoordinates;
    window.cancelAnimationFrame(requestAnimationId);
}

function animateBearing() {

  let counter = getCurIndexFromAnimationPhase();

  const start = coordinatesStream[counter >= numCoordinates ? counter - 1 : counter];
  const end = coordinatesStream[counter >= numCoordinates ? counter : counter + 1];

  if (!start || !end) return;

  curPositionBeetle.setRotation(turf.bearing(turf.point(start), turf.point(end)) + 90);

  setTimeout(() => {
    if (isPlayerStatusPlaying()) {
      animateBearing();
    }
  }, animateBearingInterval);
}

/*
This method updates the camera position and bearing when the experience player is playing.
The frame updates are paused when highlights or media are being played.
*/
function mainAnimationFrame(time) {

  if (!mainAnimationStartTime) mainAnimationStartTime = time;

  animationPhase = (time - mainAnimationStartTime) / playerDuration + lastPausedAnimationPhase;

  if (animationPhase > 1) {
    reset();
    return
  };

  let curIndex = getCurIndexFromAnimationPhase();

  let curHighlight = curHighlightEventIndex >= numHighlights ? null : experienceHighlights.highlights[curHighlightEventIndex];

  if (curHighlight && curIndex >= curHighlight["startIndex"] && !curHighlight["triggered"]) {
        log("trigging highlight");
        curHighlightEventIndex++;
        curHighlight["triggered"] = true;
        initHighlight(curHighlight["startIndex"], curHighlight["endIndex"]);
        prepareForHighlight(curHighlight["startIndex"]);
        return;
  }

  if (curPhotoEventIndex < numPhotos && curIndex >= photosIndexesSorted[curPhotoEventIndex]) {
        playPhotoEvent(photosIndexesSorted[curPhotoEventIndex++]);
        return;
  }

  //TODO this will have same format as the photos
  if (videoIndex != -1 && curIndex >= videoIndex) {
       videoIndex = -1;
       playVideoEvent();
       return;
  }

  const curCoordinates = coordinatesStream[curIndex];
  curPositionBeetle.setLngLat(curCoordinates).addTo(map);

  updateAnimationPhase(animationPhase);

  var curPosition = {
      lng: curCoordinates[0],
      lat: curCoordinates[1],
    };

  const camera = map.getFreeCameraOptions();

  camera.position = mapboxgl.MercatorCoordinate.fromLngLat(
    curPosition,
    cameraAltitude
  );

  camera.lookAtPoint(curPosition);

  map.setFreeCameraOptions(camera);

  if (isPlayerStatusPlaying()) {
    requestAnimationId = window.requestAnimationFrame(mainAnimationFrame);
  }
}


/*
enum class ExperiencePlayerStatuses {
    ERROR,
    LOADING,
    PLAYING,
    PAUSED,
    FINISHED
}
*/
window.setInterval(function () {

    if (!mapLoaded) return;

    if (isPlayerStatusFinished()) {
        resetAnimation();
    }
    else {
         if (isHighlightEventPlaying) {

            if (!isAnimationPlaying && isPlayerStatusPlaying()) {
                highlightResume();
            }
            else if (isPlayerStatusPaused()) {
                highlightPause();
            }
        }
        else {

            if (!isAnimationPlaying && isPlayerStatusPlaying()) {
                startAnimation();
            }
            else if (isPlayerStatusPaused()) {
                pauseAnimation();
            }
        }
    }

}, 10);


map.on("load", () => {
    issueMapLoaded();
    mapLoaded = true;
});