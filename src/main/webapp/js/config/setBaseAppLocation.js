(function (window, document, undefined) {
    var location = window.location || document.location,
        locationPathName = location.pathname,
        locationNesting,
        locationNesting = locationPathName.lastIndexOf('/') > 0 ? locationPathName.substring(0, locationPathName.lastIndexOf('/') + 1) : "",
        headTag = document.getElementsByTagName('head')[0],
        baseTag = document.createElement("base");
    if (locationNesting) {
        baseTag.href = locationNesting;
    } else {
        baseTag.href = "/";
    }
    headTag.appendChild(baseTag);
})(window, document);