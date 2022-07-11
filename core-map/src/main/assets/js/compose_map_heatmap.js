  mapboxgl.accessToken =
    'TODO';
  const map = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/dark-v10',
      center: [-79.8070, 11.0041],
      zoom: 1.5,
      attributionControl: false
    });

  const baseFeatureCollection = {"type":"FeatureCollection","features":[]};

  const startCoordinates = JSON.parse(ExperienceHeatMap.getStartCoordinates());

  for (var i = 0; i < startCoordinates.length; i++) {
    const isInSouth = startCoordinates[i][1] < 0;

    baseFeatureCollection["features"].push(
        {
            "type": "Feature",
            "geometry": {
              "type": "Point",
              "coordinates": startCoordinates[i]
            },
            "properties": {
              "name": "Johnny boy",
              "dbh": isInSouth ? 1000 : 100
            }
          }
    )
  };

   const heatMapRadiusFactor = 15;

  const polylines = JSON.parse(ExperienceHeatMap.getPolylines());

  map.on('load', () => {

    for (var i = 0; i < polylines.length; i++) {
    const elem = polylines[i];
    const id = "line" + i;
    const trace_id = "trace" + i;

     map.addSource(trace_id, {
                type: "geojson",
                data: {
                  type: "Feature",
                  properties: {},
                  geometry: {
                    type: "LineString",
                    coordinates: polyline.decode(elem),
                  },
                },
              });
              map.addLayer({
                type: "line",
                source: trace_id,
                id: id,
                paint: {
                  "line-color": "#F6931D",
                  "line-width": 6,
                },
                layout: {
                  "line-cap": "round",
                  "line-join": "round",
                },
              });
    }

    map.addSource('trees', {
      'type': 'geojson',
      'data': baseFeatureCollection
    });

    map.addLayer(
      {
        'id': 'trees-heat',
        'type': 'heatmap',
        'source': 'trees',
        'maxzoom': 15,
        'paint': {
          // increase weight as diameter breast height increases
          'heatmap-weight': {
            'property': 'dbh',
            'type': 'exponential',
            'stops': [
              [1, 0],
              [62, 1]
            ]
          },
          // increase intensity as zoom level increases
          'heatmap-intensity': {
            'stops': [
              [11, 1],
              [15, 3]
            ]
          },
          // use sequential color palette to use exponentially as the weight increases
          'heatmap-color': [
            'interpolate',
            ['linear'],
            ['heatmap-density'],
            0,
            'rgba(236,222,239,0)',
            0.2,
            'rgb(208,209,230)',
            0.4,
            'rgb(166,189,219)',
            0.6,
            'rgb(103,169,207)',
            0.8,
            'rgb(28,144,153)'
          ],
          // increase radius as zoom increases
          'heatmap-radius': {
            'stops': [
              [11 + heatMapRadiusFactor, 15 + heatMapRadiusFactor],
              [15 + heatMapRadiusFactor, 20 + heatMapRadiusFactor]
            ]
          },
          // decrease opacity to transition into the circle layer
          'heatmap-opacity': {
            'default': 1,
            'stops': [
              [14, 1],
              [15, 0]
            ]
          }
        }
      },
      'waterway-label'
    );

    map.addLayer(
      {
        'id': 'trees-point',
        'type': 'circle',
        'source': 'trees',
        'minzoom': 14,
        'paint': {
          // increase the radius of the circle as the zoom level and dbh value increases
          'circle-radius': {
            'property': 'dbh',
            'type': 'exponential',
            'stops': [
              [{ zoom: 15, value: 1 }, 5],
              [{ zoom: 15, value: 62 }, 10],
              [{ zoom: 22, value: 1 }, 20],
              [{ zoom: 22, value: 62 }, 50]
            ]
          },
          'circle-color': {
            'property': 'dbh',
            'type': 'exponential',
            'stops': [
              [0, 'rgba(236,222,239,0)'],
              [10, 'rgb(236,222,239)'],
              [20, 'rgb(208,209,230)'],
              [30, 'rgb(166,189,219)'],
              [40, 'rgb(103,169,207)'],
              [50, 'rgb(28,144,153)'],
              [60, 'rgb(1,108,89)']
            ]
          },
          'circle-stroke-color': 'white',
          'circle-stroke-width': 1,
          'circle-opacity': {
            'stops': [
              [14, 0],
              [15, 1]
            ]
          }
        }
      },
      'waterway-label'
    );
  });

  // click on tree to view dbh in a popup
  map.on('click', 'trees-point', (event) => {
    new mapboxgl.Popup()
      .setLngLat(event.features[0].geometry.coordinates)
      .setHTML(`<strong>DBH:</strong> ${event.features[0].properties.dbh}`)
      .addTo(map);
  });


  map.on('click', 'trees-heat', (event) => {
    const coords = event.features[0].geometry.coordinates;
      const polylineToZoom = ExperienceHeatMap.getClosestPolylineForClick(coords[1], coords[0]);

      fitToCoordinatesHeatmap(polyline.decode(polylineToZoom));
    });
