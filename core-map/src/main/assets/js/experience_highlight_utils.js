
class ExperienceHighlight {
    var triggered = false;
    constructor(startIndex, endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}

class ExperienceHighlightSet {
    constructor(experienceHighlightSetId, highlights) {
        this.experienceHighlightSetId = experienceHighlightSetId;
        this.highlights = highlights;
    }
}
