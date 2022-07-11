const routes = JSON.parse(
  SlideInterface.getCoordinatesStreamString()
);

const cameraAltitude = 3000;

const initialCoordinates = JSON.parse(SlideInterface.getStartLngLatString());

var currentLat = initialCoordinates[1];
var currentLng = initialCoordinates[0];

var currentPhotos = {};

mapboxgl.accessToken =
  "TODO";
const map = new mapboxgl.Map({
  container: "map",
  zoom: 13.5,
  center: initialCoordinates,
  pitch: 0,
  bearing: 0,
  style: "mapbox://styles/frezafoltran/cl0k7xz3x001814mnw9nuw3kf",
  interactive: false,
});

function frame(time) {

  var newLat = SlideInterface.getCurrentLat();
  var newLng = SlideInterface.getCurrentLng();

  if (newLat != -1000.0 && newLng != -1000.0 && (newLat != currentLat || newLng != currentLng)) {
      currentLat = newLat;
      currentLng = newLng;
      setCameraAt(newLat, newLng);
  }
  window.requestAnimationFrame(frame);
}


function setCameraAt(lat, lng) {
  const camera = map.getFreeCameraOptions();

  camera.position = mapboxgl.MercatorCoordinate.fromLngLat(
    {
      lng: lng,
      lat: lat,
    },
    cameraAltitude
  );

  camera.lookAtPoint({
    lng: lng,
    lat: lat,
  });

  map.setFreeCameraOptions(camera);
}

map.on("load", () => {

    map.addSource("trace", {
        type: "geojson",
        data: {
          type: "Feature",
          properties: {},
          geometry: {
            type: "LineString",
            coordinates: routes,
          },
        },
      });
      map.addLayer({
        type: "line",
        source: "trace",
        id: "line",
        paint: {
          "line-color": "#F6931D",
          "line-width": 6,
        },
        layout: {
          "line-cap": "round",
          "line-join": "round",
        },
      });
    window.requestAnimationFrame(frame);

    window.setInterval(function () {

        if (SlideInterface.hasPendingChanges()) {
          let newPhotosDic = JSON.parse(SlideInterface.getCurrentPhotosString());
          let out = getDifferenceBetweenPhotos(newPhotosDic);

          out["toAdd"].forEach(elem => addPhoto(elem.id, elem.lat, elem.lon));
          out["toRemove"].forEach(elem => removePhoto(elem.id));

          currentPhotos = newPhotosDic;
          SlideInterface.resetPendingChanges();
        }

    }, 100);
});


function getDifferenceBetweenPhotos(newPhotos) {
    let toAdd = [];
    let toRemove = [];

    for (const [id, elem] of Object.entries(Object.assign({}, newPhotos, currentPhotos))) {

        if (id in newPhotos && !(id in currentPhotos)) {
            toAdd.push(elem);
        }
        else if (id in currentPhotos && !(id in newPhotos)) {
            toRemove.push(elem);
        }
    }

    return {
        "toAdd": toAdd,
        "toRemove": toRemove
    };
}


