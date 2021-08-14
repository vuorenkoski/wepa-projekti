function highlightNavlink(link) {
    links = document.querySelectorAll("nav a")
    for (var i = 0; i < links.length; i++) {
        if (links[i].id == link) {
            links[i].classList.add("active")
        } else {
            links[i].classList.remove("active")
        }
    }
    clearContents()
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

function clearContents() {
    var element = document.getElementById("contents1")
    while (element.firstChild) {
      element.removeChild(element.lastChild);
    }
    element = document.getElementById("contents2")
    while (element.firstChild) {
      element.removeChild(element.lastChild);
    }
}
