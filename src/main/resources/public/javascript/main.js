function highlightNavlink(link) {
    links = document.querySelectorAll("nav a")
    for (var i = 0; i < links.length; i++) {
        if (links[i].id == link) {
            links[i].classList.add("active")
        } else {
            links[i].classList.remove("active")
        }
    }
}

function divElementWithChild(classParameter, child) {
    var element = document.createElement("div")
    element.classList.add(classParameter)
    element.appendChild(child)
    return element;
}

function emptyRow() {
    var element = document.createElement("p")
    return divElementWithChild("row", element)
}

messageTab()

