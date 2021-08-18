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

function divElement(classParameter) {
    var element = document.createElement("div")
    element.classList.add(classParameter)
    return element;
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

function formatDate(d) {
    datestr = [d.slice(8, 10), d.slice(5, 7), d.slice(0, 4)].join(".")
    return [datestr, "klo", d.slice(11,16)].join(" ")
}

function compareComments(a,b) {
    if (a.date < b.date){
        return -1
    }
    if (a.date > b.date ){
        return 1
    }
    return 0
}

function clearNode(node) {
    while (node.firstChild) {
      node.removeChild(node.lastChild);
    }
}