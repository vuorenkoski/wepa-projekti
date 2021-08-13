var httpGetSettings = new XMLHttpRequest()

function settingsTab() {
    highlightNavlink("settingsTab")
    httpGetSettings.open("GET",contextRoot + "settings")
    httpGetSettings.send()
    var root = document.getElementById("contents")
    root.innerHTML = "asetukset"
}

