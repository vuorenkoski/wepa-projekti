var httpGetFollowers = new XMLHttpRequest()

function followersTab() {
    highlightNavlink("followersTab")
    httpGetFollowers.open("GET",contextRoot + "followers")
    httpGetFollowers.send()
    var root = document.getElementById("contents")
    root.innerHTML = "seuraajat"
}
