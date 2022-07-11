/*
This file contains the classes to model data coming from the js interface
*/

class PathHighlight {
    constructor(startIndex, endIndex, triggered) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.triggered = triggered;
    }
}

class PathHighlightSet {
    constructor(id, highlights) {
        this.id = id;
        this.highlights = highlights;
    }
}


class MapIcon {
    constructor(id, lat, lon, base64) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.base64 = base64;
    }
}