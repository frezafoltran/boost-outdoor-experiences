/*
This file uses the data from setup.js and the helper function from render_utils to render
the data in the map.
*/

const el = document.createElement('div');
el.className = 'logo-beetle';

const curPositionBeetle = new mapboxgl.Marker(el);

map.on("load", () => {

    addCoordinatesStreamPolyline(coordinatesStream, defaultLineColor);

    addExperienceHighlights(experienceHighlights, coordinatesStream, highlightLineColor);

    for (const [indexInStream, elem] of Object.entries(photosByIndexInCoordinateStream)){
         addIcon(elem.lat, elem.lon, elem.base64);
    }

    for (const [indexInStream, elem] of Object.entries(videosByIndexInCoordinateStream)){
         addIcon(elem.lat, elem.lon, elem.base64);
    }
});