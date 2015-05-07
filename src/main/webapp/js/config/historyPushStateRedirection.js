if (!history.pushState) {
    //if hashbang url found and we are not on our base then go to base
    if (window.location.hash.charAt(1) === "!" && window.location.pathname !== '/') {
        window.location.replace('/#!' + window.location.hash.substring(2));
    }
    //if hasbang not found then convert link to hashbang mode
    if (window.location.hash.charAt(1) !== "!") {
        window.location.replace('/#!' + window.location.pathname + window.location.search + window.location.hash);
    }
}