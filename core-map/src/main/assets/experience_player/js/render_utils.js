/*
Helpers to add map data. this script requires that the mapbox map object is setup in another script
*/

function addCoordinatesStreamPolyline(stream, lineColor) {

    map.addSource("trace", {
        type: "geojson",
        data: {
          type: "Feature",
          properties: {},
          geometry: {
            type: "LineString",
            coordinates: stream,
          },
        },
      });
  map.addLayer({
    type: "line",
    source: "trace",
    id: "line",
    paint: {
      "line-color": lineColor,
      "line-width": 4,
    },
    layout: {
      "line-cap": "round",
      "line-join": "round",
    },
  });
}

function addExperienceHighlights(highlightsValues, baseStream, lineColor) {

    for (const elem of highlightsValues.highlights){

        const highlightId = highlightsValues.id + "-" + elem.startIndex + "-" + elem.endIndex;

        map.addSource(highlightId, {
            type: "geojson",
            data: {
              type: "Feature",
              properties: {},
              geometry: {
                type: "LineString",
                coordinates: baseStream.slice(elem.startIndex, elem.endIndex),
              },
            },
          });

          map.addLayer({
            type: "line",
            source: highlightId,
            id: highlightId,
            paint: {
              "line-color": lineColor,
              "line-width": 6,
            },
            layout: {
              "line-cap": "round",
              "line-join": "round",
            },
          });
    }
}

function addIcon(lat, lon, base64) {
    const el = document.createElement('img');
    el.className = 'icon-marker';
    el.src = base64;

    const marker = new mapboxgl.Marker(el);

    marker.setLngLat([lon, lat]).addTo(map);
}

