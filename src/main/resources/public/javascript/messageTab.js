var httpSendMessages = new XMLHttpRequest()

function sendMessage() {
    var data = {message: document.getElementById("newMessage").value}
    httpSendMessages.open("POST",contextRoot + "api/messages")
    httpSendMessages.setRequestHeader("Content-type", "application/json");
    httpSendMessages.send(JSON.stringify(data))  
    document.getElementById("newMessage").value = ""
}

httpSendMessages.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGetMessages.open("GET",contextRoot + "api/messages")
    httpGetMessages.send()
}

var httpGetMessages = new XMLHttpRequest()

httpGetMessages.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    clearContents()

    var root = document.getElementById("contents1")

    textAreaNode = document.createElement("textarea")
    textAreaNode.setAttribute("id", "newMessage")
    textAreaNode.setAttribute("rows", "4")
    textAreaNode.setAttribute("cols", "50")
    var newMessageColNode = divElementWithChild("col-sm-12", textAreaNode)
    var newMessageRowNode = divElementWithChild("row", newMessageColNode)

    var dbtn = document.createElement("input");
    dbtn.setAttribute("type", "button")
    dbtn.setAttribute("value", "Julkaise uusi viesti")
    dbtn.setAttribute("onclick", "sendMessage()")
    var buttonColNode = divElementWithChild("col-sm-12", dbtn)
    var buttonRowNode = divElementWithChild("row", buttonColNode)

    root.appendChild(newMessageRowNode)
    root.appendChild(buttonRowNode)
    root.appendChild(emptyRow())

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        // Main container for message
        var mainColNode = divElement("col-sm-12")
        var mainRowNode = divElementWithChild("row", mainColNode)

        // Full name row
        var nameRowNode = divElement("row")

        var name = document.createElement("h5")
        name.innerHTML = data[i].profile.fullname
        var nameNode = divElementWithChild("col-sm-4", name)

        var dateNode = divElementWithChild("col-sm-4", document.createTextNode(formatDate(data[i].date)))
        var likesNode = divElementWithChild("col-sm-2", document.createTextNode("" + data[i].numberOfLikes + " tykkäystä"))

        var btn = document.createElement("input");
        btn.setAttribute("type", "button")
        btn.setAttribute("value", "Tykkää")
        btn.setAttribute("onclick", "likeMessage("+ data[i].id + ")")
        var buttonNode = divElementWithChild("col-sm-2", btn)

        nameRowNode.appendChild(nameNode)
        nameRowNode.appendChild(dateNode)
        nameRowNode.appendChild(likesNode)
        nameRowNode.appendChild(buttonNode)
        mainColNode.appendChild(nameRowNode)

        // Message row
        var messageColNode = divElement("col-sm-12")
        var messageRowNode = divElementWithChild("row", messageColNode)
        var messageNode = document.createElement("p")
        messageNode.innerHTML = data[i].message
        messageColNode.appendChild(messageNode)
        mainColNode.appendChild(messageRowNode)

        // container for comments
        var commentRootRowNode = divElement("row")
        var commentColNode = divElement("col-sm-11")
        commentRootRowNode.appendChild(divElement("col-sm-1"))
        commentRootRowNode.appendChild(commentColNode)

        // add all comments
        addComments(commentColNode, data[i].messageComments)

        // new comment form
        var newCommentRowNode = divElement("row")
        var newCommentNode = document.createElement("textarea")
        newCommentNode.setAttribute("id", "comment" + data[i].id)
        newCommentNode.setAttribute("rows", "1")
        newCommentNode.setAttribute("cols", "30")
        newCommentRowNode.appendChild(newCommentNode)
        var cbtn = document.createElement("input");
        cbtn.setAttribute("type", "button")
        cbtn.setAttribute("value", "Lähetä kommentti")
        cbtn.setAttribute("onclick", "addComment("+ data[i].id + ")")
        newCommentRowNode.appendChild(cbtn)
        commentColNode.appendChild(newCommentRowNode)

        // comment container to main container
        mainColNode.appendChild(commentRootRowNode)

        // extra empty rows at the end
        mainColNode.appendChild(emptyRow())
        mainColNode.appendChild(emptyRow())

        root.appendChild(mainRowNode)
    }
}

function addComments(root, commentdata) {
    commentdata.sort(compareComments)
    var k = 0
    if (commentdata.length>10) {
        k = commentdata.length - 10
    }
    for (var i = k; i < commentdata.length; i++) {
        var nameRowNode = divElement("row")
        var nameNode = document.createElement("b")
        nameNode.innerHTML = commentdata[i].profile.fullname
        var nameColNode = divElementWithChild("col-sm-4", nameNode)
        var dateColNode = divElementWithChild("col-sm-4", document.createTextNode(formatDate(commentdata[i].date)))
        nameRowNode.append(nameColNode)
        nameRowNode.append(dateColNode)
        root.appendChild(nameRowNode)

        var messageNode = document.createElement("p")
        messageNode.innerHTML = commentdata[i].comment
        var messageColNode = divElementWithChild("col-sm-12", messageNode)
        var messageRowNode = divElementWithChild("row", messageColNode)
        root.appendChild(messageRowNode)
    }
}

function messageTab() {
    highlightNavlink("messageTab")
    httpGetMessages.open("GET",contextRoot + "api/messages")
    httpGetMessages.send()
}

function likeMessage(id) {
    console.log("tykkäys: " + id)
    httpSendMessages.open("POST",contextRoot + "api/messages/" + id + "/likes")
    httpSendMessages.setRequestHeader("Content-type", "application/json");
    httpSendMessages.send() 
}

function addComment(id) {
    var data = {comment: document.getElementById("comment" + id).value}
    httpSendMessages.open("POST",contextRoot + "api/messages/" + id + "/comments")
    httpSendMessages.setRequestHeader("Content-type", "application/json");
    httpSendMessages.send(JSON.stringify(data))  
    document.getElementById("comment" + id).value = ""
}
