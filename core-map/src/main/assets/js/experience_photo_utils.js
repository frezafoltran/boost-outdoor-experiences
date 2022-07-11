class Photo {
    constructor(id, url, lat, lon, indexInStream) {
        this.id = id;
        this.url = url;
        this.lat = lat;
        this.lon = lon;
        this.indexInStream = indexInStream;
    }
}


function addVideo(lat, lng) {

 const el = document.createElement('img');
    el.className = 'video-marker-square';
    //el.src = 'https://i.ibb.co/0y1T3RC/video-camera.png'
    el.src = videoIconsBase64ById["sample"];
AndroidMapPlayer.logS("" + videoIconsBase64ById["sample"]);

    const videoMarker = new mapboxgl.Marker(el);

    videoMarker.setLngLat([lng, lat]).addTo(map);
}

function addPhoto(photoId, lat, lng) {

    let photoData = {
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: {},
          geometry: {
            type: "Point",
            coordinates: [lng, lat],
          },
        },
      ],
    }

    map.addSource(photoId, {
              type: "geojson",
              data: photoData,
            });

      map.addLayer({
          id: photoId,
          source: photoId,
          type: "symbol",
          layout: {
            "icon-image": "attraction",
            "icon-rotate": ["get", "bearing"],
            "icon-rotation-alignment": "map",
            "icon-allow-overlap": false,
            "icon-ignore-placement": true,
            "icon-size": 1.5,
          },
        });
}

function removePhoto(photoId){
    map.removeLayer(photoId);
    map.removeSource(photoId);
}

function addPhotoDiv(lat, lng) {

const el = document.createElement('img');
    el.className = 'marker-basis';
    el.src = 'https://i.ibb.co/brT1JBX/photo-camera-interface-symbol-for-button.png'


    const photoMarker = new mapboxgl.Marker(el);

    photoMarker.setLngLat([lng, lat]).addTo(map);
}
