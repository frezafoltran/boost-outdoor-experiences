/*
This file sets up the annotations for the experience player.
It also provides the necessary data for experience_player/js/main.js to setup the player
*/

// Data from JS interface
const coordinatesStream = JSON.parse(ExperiencePlayer.getCoordinateStream());
const numCoordinates = coordinatesStream.length * 1.0;

const experienceHighlights = JSON.parse(ExperiencePlayer.getExperienceHighlights());
const numHighlights = experienceHighlights.highlights.length;

const photosByIndexInCoordinateStream = JSON.parse(ExperiencePlayer.getPhotosByIndexInCoordinateStream());
const photosIndexesSorted = JSON.parse(ExperiencePlayer.getPhotosIndexesSorted());
const numPhotos = photosIndexesSorted.length;

const videosByIndexInCoordinateStream = JSON.parse(ExperiencePlayer.getVideosByIndexInCoordinateStream());
const videosIndexesSorted = JSON.parse(ExperiencePlayer.getVideosIndexesSorted());

const defaultLineColor = ExperiencePlayer.getDefaultLineColor();

const highlightLineColor = ExperiencePlayer.getHighlightLineColor();

const playerDuration = ExperiencePlayer.getPlayerDuration();

function isPlayerStatusPlaying() {
    return ExperiencePlayer.isPlayerStatusPlaying();
}

function isPlayerStatusPaused() {
    return ExperiencePlayer.isPlayerStatusPaused();
}

function isPlayerStatusFinished() {
    return ExperiencePlayer.isPlayerStatusFinished();
}

function playPhotoEvent(photoIndex) {
    ExperiencePlayer.playPhotoEvent(photoIndex);
}

function playVideoEvent() {
    ExperiencePlayer.playVideoEvent();
}

function playHighlightEvent() {
    ExperiencePlayer.playHighlightEvent();
}

function finishHighlightEvent() {
    ExperiencePlayer.finishHighlightEvent();
}

function updateAnimationPhase(phase) {
    ExperiencePlayer.updateAnimationPhase(phase);
}

function issueMapLoaded() {
    ExperiencePlayer.issueMapLoaded();
}

function log(s) {
    ExperiencePlayer.logString("Experience Player " + s);
}

// Mapbox setup
mapboxgl.accessToken =
  "TODO";
const map = new mapboxgl.Map({
  container: "map",
  zoom: 13.5,
  center: coordinatesStream[0],
  pitch: 0,
  bearing: 0,
  style: "mapbox://styles/frezafoltran/cl0k7xz3x001814mnw9nuw3kf",
  interactive: false,
});
