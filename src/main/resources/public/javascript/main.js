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
    var element = document.getElementById("contents")
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

function addComments(root, commentdata) {
    commentdata.sort(compareComments)
    var k = 0
    if (commentdata.length>10) {
        k = commentdata.length - 10
    }
    for (var i = k; i < commentdata.length; i++) {
        addComment(root, commentdata[i])
    }
}

function addComment(root, comment) {
    var nameRowNode = divElement("row")
    var nameNode = document.createElement("b")
    nameNode.innerHTML = comment.profile.fullname
    var nameColNode = divElementWithChild("col-sm-4", nameNode)
    var dateColNode = divElementWithChild("col-sm-4", document.createTextNode(formatDate(comment.date)))
    nameRowNode.appendChild(nameColNode)
    nameRowNode.appendChild(dateColNode)
    root.appendChild(nameRowNode)

    var messageNode = document.createElement("p")
    messageNode.innerHTML = comment.comment
    var messageColNode = divElementWithChild("col-sm-12", messageNode)
    var messageRowNode = divElementWithChild("row", messageColNode)
    root.appendChild(messageRowNode)
}