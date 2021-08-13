var httpSend = new XMLHttpRequest()

function sendMessage() {
    var data = {message: document.getElementById("newMessage").value}
    httpSend.open("POST",contextRoot + "messages")
    httpSend.setRequestHeader("Content-type", "application/json");
    httpSend.send(JSON.stringify(data))  
    document.getElementById("newMessage").value = ""
}

httpSend.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGet.open("GET",contextRoot + "messages")
    httpGet.send()
}

var httpGet = new XMLHttpRequest()

httpGet.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    var root = document.getElementById("messagelist")

    while (root.firstChild) {
      root.removeChild(root.lastChild);
    }

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
        // Main container for messages
        var mainRowNode = document.createElement("div")
        mainRowNode.classList.add("row")
        var mainColNode = document.createElement("div")
        mainColNode.classList.add("col-sm-12")
        mainRowNode.appendChild(mainColNode)

        // Full name row
        var nameRowNode = document.createElement("div")
        nameRowNode.classList.add("row")
        var nameNode = document.createElement("div")
        nameNode.classList.add("col-sm-4")
        var name = document.createElement("h5")
        name.innerHTML = data[i].profile.fullname
        nameNode.appendChild(name)

        var dateNode = document.createElement("div")
        dateNode.classList.add("col-sm-4")
        dateNode.appendChild(document.createTextNode(formatDate(data[i].date)))
        var likesNode = document.createElement("div")
        likesNode.classList.add("col-sm-2")
        likesNode.appendChild(document.createTextNode("xxx tykkäystä"))
        var buttonNode = document.createElement("div")
        buttonNode.classList.add("col-sm-2")
        var btn = document.createElement("input");
        btn.setAttribute("type", "button")
        btn.setAttribute("value", "Tykkää")
        btn.setAttribute("onclick", "likeMessage("+ data[i].id + ")")
        buttonNode.appendChild(btn)

        nameRowNode.appendChild(nameNode)
        nameRowNode.appendChild(dateNode)
        nameRowNode.appendChild(likesNode)
        nameRowNode.appendChild(buttonNode)
        mainColNode.appendChild(nameRowNode)

        // Message row
        var messageRowNode = document.createElement("div")
        messageRowNode.classList.add("row")
        var messageColNode = document.createElement("div")
        messageColNode.classList.add("col-sm-12")
        messageRowNode.appendChild(messageColNode)
        var messageNode = document.createElement("p")
        messageNode.innerHTML = data[i].message
        messageColNode.appendChild(messageNode)
        mainColNode.appendChild(messageRowNode)

        // container for comments
        var commentRootRowNode = document.createElement("div")
        commentRootRowNode.classList.add("row")
        var commentEmptyColNode = document.createElement("div")
        commentEmptyColNode.classList.add("col-sm-1")
        var commentColNode = document.createElement("div")
        commentColNode.classList.add("col-sm-11")
        commentRootRowNode.appendChild(commentEmptyColNode)
        commentRootRowNode.appendChild(commentColNode)

        // add all comments
        addComments(commentColNode, data[i].messageComments)

        // new comment form
        var newCommentRowNode = document.createElement("div")
        newCommentRowNode.classList.add("row")
        newCommentNode = document.createElement("textarea")
        newCommentNode.setAttribute("id", "comment" + data[i].id)
        newCommentNode.setAttribute("rows", "1")
        newCommentNode.setAttribute("cols", "30")
        var cbtn = document.createElement("input");
        cbtn.setAttribute("type", "button")
        cbtn.setAttribute("value", "Lähetä kommentti")
        cbtn.setAttribute("onclick", "addComment("+ data[i].id + ")")
        newCommentRowNode.appendChild(newCommentNode)
        newCommentRowNode.appendChild(cbtn)
        commentColNode.appendChild(newCommentRowNode)

        // comment container to main container
        mainColNode.appendChild(commentRootRowNode)

        // extra empty row at the end
        var emptyRowNode = document.createElement("div")
        emptyRowNode.classList.add("row")
        emptyRowNode.appendChild(document.createElement("br"))
        mainColNode.appendChild(emptyRowNode)

        root.appendChild(mainRowNode)
    }
}

function addComments(root, commentdata) {
    for (j=0; j<commentdata.length; j++) {
        var nameRowNode = document.createElement("div")
        nameRowNode.classList.add("row")
        var nameColNode = document.createElement("div")
        nameColNode.classList.add("col-sm-4")
        var nameNode = document.createElement("b")
        nameNode.innerHTML = commentdata[j].profile.fullname
        nameColNode.appendChild(nameNode)
        var dateColNode = document.createElement("div")
        dateColNode.classList.add("col-sm-4")
        dateColNode.appendChild(document.createTextNode(formatDate(commentdata[j].date)))
        nameRowNode.append(nameColNode)
        nameRowNode.append(dateColNode)
        root.appendChild(nameRowNode)

        var messageRowNode = document.createElement("div")
        messageRowNode.classList.add("row")
        var messageColNode = document.createElement("div")
        messageColNode.classList.add("col-sm-12")
        messageRowNode.appendChild(messageColNode)
        var messageNode = document.createElement("p")
        messageNode.innerHTML = commentdata[j].comment
        messageColNode.appendChild(messageNode)
        root.appendChild(messageRowNode)
    }
}

httpGet.open("GET",contextRoot + "messages")
httpGet.send()

function likeMessage(id) {
    console.log("tykkäys: " + id)
}

function addComment(id) {
    var data = {comment: document.getElementById("comment" + id).value}
    httpSend.open("POST",contextRoot + "messages/" + id + "/comments")
    httpSend.setRequestHeader("Content-type", "application/json");
    httpSend.send(JSON.stringify(data))  
    document.getElementById("comment" + id).value = ""
}

function formatDate(d) {
    datestr = [d.slice(8, 10), d.slice(5, 7), d.slice(0, 4)].join(".")
    return [datestr, "klo", d.slice(11,16)].join(" ")
}
