const routes = JSON.parse(
  AndroidMapPlayer.getCoordinatesStreamSimplifiedString()
);

const videoIconsBase64ById = JSON.parse(AndroidMapPlayer.getVideoIconsBase64ById());

const experienceHighlights = JSON.parse(AndroidMapPlayer.getExperienceHighlightsString());
const numberOfExperienceHighlights = experienceHighlights.highlights.length;

const photosByIndexInStream = JSON.parse(AndroidMapPlayer.getPhotosMapString());

const photosIndexes = JSON.parse(AndroidMapPlayer.getPhotosIndexListString());

AndroidMapPlayer.logS("JVFF" + photosIndexes);

var videoIndex = 300;

var nextPhotoIndexToShow = 0;

function addExperienceHighlight(experienceHighlight) {

    const highlightId = "" + experienceHighlight.startIndex + "-" + experienceHighlight.endIndex;

    map.addSource(highlightId, {
        type: "geojson",
        data: {
          type: "Feature",
          properties: {},
          geometry: {
            type: "LineString",
            coordinates: routes.slice(experienceHighlight.startIndex, experienceHighlight.endIndex),
          },
        },
      });

      map.addLayer({
        type: "line",
        source: highlightId,
        id: highlightId,
        paint: {
          "line-color": AndroidMapPlayer.getHighlightLineColor(),
          "line-width": 6,
        },
        layout: {
          "line-cap": "round",
          "line-join": "round",
        },
      });
}

var flying = false;

mapboxgl.accessToken =
  "TODO";
const map = new mapboxgl.Map({
  container: "map",
  zoom: 13.5,
  center: JSON.parse(AndroidMapPlayer.getStartLngLatString()),
  pitch: 0,
  bearing: 0,
  style: "mapbox://styles/frezafoltran/cl0k7xz3x001814mnw9nuw3kf",
  interactive: false,
});

var curHighlightEventIndex = 0;

const targetRoute = routes;
const cameraRoute = targetRoute;

const animationDuration = AndroidMapPlayer.getAnimationDuration();
const cameraAltitude = AndroidMapPlayer.getCameraAltitude();
const routeDistance = turf.lineDistance(turf.lineString(targetRoute));
const cameraRouteDistance = turf.lineDistance(turf.lineString(cameraRoute));

function setInitialCamera() {
  map.flyTo({
    center: JSON.parse(AndroidMapPlayer.getStartLngLatString()),
    zoom: 13.5,
  });
}

map.on("load", () => {
  map.addSource("trace", {
    type: "geojson",
    data: {
      type: "Feature",
      properties: {},
      geometry: {
        type: "LineString",
        coordinates: targetRoute,
      },
    },
  });
  map.addLayer({
    type: "line",
    source: "trace",
    id: "line",
    paint: {
      "line-color": AndroidMapPlayer.getDefaultLineColor(),
      "line-width": 4,
    },
    layout: {
      "line-cap": "round",
      "line-join": "round",
    },
  });

    for (const [indexInStream, elem] of Object.entries(photosByIndexInStream)){
        addPhotoDiv(elem.lat, elem.lon);
    }

    addVideo(routes[videoIndex][1], routes[videoIndex][0]);

  for (const elem of experienceHighlights.highlights){
          addExperienceHighlight(elem);
      }

  setInitialCamera();
});


var coordinatesRouteCurIndex = 0;

var counter = 0;
var steps = targetRoute.length;
var phase = 0;
let start;
var lastPausedPhase = 0;
let requestAnimationId;
var isMapAlreadyPlaying = false;
var mapLoaded = false;

function reset() {
    window.cancelAnimationFrame(requestAnimationId);
  isMapAlreadyPlaying = false;
  lastPausedPhase = 0;
  phase = 0;
  start = null;
}

function getCurIndexFromPhase() {
    return parseInt(routes.length * phase);
}

const interval = parseFloat(animationDuration) / parseFloat(targetRoute.length);

function arePointsMatch(p1, p2){
    return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]) < 0.000001
}

function animateBearing() {

  let counter = getCurIndexFromPhase();

  const start = targetRoute[counter >= steps ? counter - 1 : counter];
  const end = targetRoute[counter >= steps ? counter : counter + 1];

  if (!start || !end) return;

  highlightMarker.setRotation(turf.bearing(turf.point(start), turf.point(end)) + 90);

  setTimeout(() => {
    if (AndroidMapPlayer.isMapPlaying()) {
      animateBearing();
    }
  }, 100);
}

function frame(time) {

  if (!start) start = time;

  phase = (time - start) / animationDuration + lastPausedPhase;

  if (phase > 1) {
    reset();
    return
  };


  let curIndex = getCurIndexFromPhase();
  let curHighlight = curHighlightEventIndex >= numberOfExperienceHighlights ? null : experienceHighlights.highlights[curHighlightEventIndex];
  if (curHighlight && curIndex >= curHighlight["startIndex"] && !curHighlight["triggered"]) {
        curHighlightEventIndex++;
        curHighlight["triggered"] = true;
        initHighlight(curHighlight["startIndex"], curHighlight["endIndex"]);
        prepareForHighlight(curHighlight["startIndex"])
        return;
  }

  if (nextPhotoIndexToShow < photosIndexes.length && curIndex >= photosIndexes[nextPhotoIndexToShow]) {
        AndroidMapPlayer.showPhotoAtIndex(photosIndexes[nextPhotoIndexToShow++])
  }

  if (videoIndex != -1 && curIndex >= videoIndex) {
            videoIndex = -1;
          AndroidMapPlayer.showVideo();
    }

  highlightMarker.setLngLat(routes[curIndex]).addTo(map);

  AndroidMapPlayer.updateCurPhase(phase);

  const camera = map.getFreeCameraOptions();

  var curPosition = {
      lng: routes[curIndex][0],
      lat: routes[curIndex][1],
    };
  camera.position = mapboxgl.MercatorCoordinate.fromLngLat(
    curPosition,
    cameraAltitude
  );

  camera.lookAtPoint(curPosition);

  map.setFreeCameraOptions(camera);

  if (AndroidMapPlayer.isMapPlaying()) {
    requestAnimationId = window.requestAnimationFrame(frame);
  }
}

function pause() {
    isMapAlreadyPlaying = false;
    lastPausedPhase = phase;
    window.cancelAnimationFrame(requestAnimationId);
}

function prepareForHighlight(_startIndexOfHighlight) {
    isMapAlreadyPlaying = false;
    lastPausedPhase = (_startIndexOfHighlight * 1.0) / (steps * 1.0);
    window.cancelAnimationFrame(requestAnimationId);
}

function startMapAnimation() {
     isMapAlreadyPlaying = true;
     start = null;
     animateBearing();
     requestAnimationId = window.requestAnimationFrame(frame);
}

window.setInterval(function () {

    if (!mapLoaded) return;

      if (!isMapAlreadyPlaying && AndroidMapPlayer.isMapPlaying()) {
        startMapAnimation();
      } else if (AndroidMapPlayer.isMapPaused()) {
        pause();
        highlightPause();
      }
      else if (AndroidMapPlayer.isMapBlocked()) {
        pause();
        highlightPause();
      }
      else if (AndroidMapPlayer.isMapStopped()) {
        reset();
      }
      else if (AndroidMapPlayer.isMapHighlight()) {
        if (!isMapAlreadyHighlighting) {
            highlightResume();
        }
      }
}, 10);


map.on("load", () => {
    AndroidMapPlayer.issueMapLoaded()
    mapLoaded = true;
});
