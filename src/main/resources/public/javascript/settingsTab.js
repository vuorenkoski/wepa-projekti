function settingsTab() {
    highlightNavlink("settingsTab")
    var root = document.getElementById("contents")

    var link = document.createElement("a")
    link.href = "/profile"
    link.innerHTML = "Muuta profiilin tietoja"
    var linkRow = divElementWithChild("row", link)
    root.appendChild(linkRow)
    root.appendChild(emptyRow())
    root.appendChild(emptyRow())
}