function fitToCoordinates(coordinates){

    originalZoom = map.getZoom();

    const bounds = new mapboxgl.LngLatBounds(
        coordinates[0],
        coordinates[0]
    );

    for (const coord of coordinates) {
        bounds.extend(coord);
    };

    map.fitBounds(bounds, {
        padding: {
            top: 50,
            bottom: 200,
            left: 50,
            right: 50
        }
    });
}

function fitToCoordinatesHeatmap(coordinates){

    originalZoom = map.getZoom();

    const bounds = new mapboxgl.LngLatBounds(
        coordinates[0],
        coordinates[0]
    );

    for (const coord of coordinates) {
        bounds.extend(coord);
    };


    map.fitBounds(bounds, {
        padding: {
            top: 30,
            bottom: 30,
            left: 30,
            right: 30
        },
        easing: function (t) {
                return 1 - Math.pow(1 - t, 5);
                }
    });
}