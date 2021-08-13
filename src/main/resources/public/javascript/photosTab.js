var httpGetPhotos = new XMLHttpRequest()

function photosTab() {
    highlightNavlink("photosTab")
    httpGetPhotos.open("GET",contextRoot + "photos")
    httpGetPhotos.send()
    var root = document.getElementById("contents1")
    root.innerHTML = "kuvat"
}

