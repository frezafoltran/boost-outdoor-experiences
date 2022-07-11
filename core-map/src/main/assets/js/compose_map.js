  mapboxgl.accessToken =
    'TODO';
  const map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/dark-v10',
    center: [-76.8070, 11.0041],
    zoom: 1.5,
    attributionControl: false
  });

const startCoordinates = JSON.parse(ExperienceHeatMap.getStartCoordinates());

function addStartIcon(lat, lng) {

 const el = document.createElement('div');
    el.className = 'marker-start-icon';

    const marker = new mapboxgl.Marker(el);

    marker.setLngLat([lng, lat]).addTo(map);
}

map.on("load", () => {

    ExperienceHeatMap.logS(" " + startCoordinates);
    for (var i = 0; i < startCoordinates.length; i++) {
        addStartIcon(startCoordinates[i][1], startCoordinates[i][0]);
    }
})