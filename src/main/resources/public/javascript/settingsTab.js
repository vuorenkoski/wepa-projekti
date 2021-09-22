function settingsTab() {
    highlightNavlink("settingsTab")
    var root = document.getElementById("contents")

    var link = document.createElement("a")
    link.href = "/profile"
    link.innerHTML = "Muuta profiilin tietoja"
    var linkRow = divElementWithChild("row", link)
    root.appendChild(linkRow)

    var help = document.createElement("a")
    help.href = "/help"
    help.innerHTML = "Käyttöohjeet"
    var helpRow = divElementWithChild("row", help)
    root.appendChild(emptyRow())
    root.appendChild(helpRow)

    root.appendChild(emptyRow())
    root.appendChild(emptyRow())
}