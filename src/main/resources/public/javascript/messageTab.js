var httpSendMessage = new XMLHttpRequest()
var httpGetMessages = new XMLHttpRequest()
var httpSendMessageLike = new XMLHttpRequest()
var httpSendMessageComment = new XMLHttpRequest()

httpSendMessage.onreadystatechange = function() {
    if (this.readyState!=4) {
        return
    }
    if  (this.status!=201) {
        var data = JSON.parse(this.responseText)
        alert(data.message)
        return
    }
    var data = JSON.parse(this.responseText)
    document.getElementById("messageBlock").prepend(createMessageElement(data))
}

httpSendMessageLike.onreadystatechange = function() {
    if (this.readyState!=4) {
        return
    }
    if  (this.status!=201) {
        var data = JSON.parse(this.responseText)
        alert(data.message)
        return
    }
    var message = JSON.parse(this.responseText)
    document.getElementById("likesNode" + message.id).innerHTML = "" + message.numberOfLikes + " tykkäystä"
}

httpSendMessageComment.onreadystatechange = function() {
    if (this.readyState!=4) {
        return
    }
    if  (this.status!=201) {
        var data = JSON.parse(this.responseText)
        alert(data.message)
        return
    }
    var data = JSON.parse(this.responseText)
    addComment(document.getElementById("commentsBlock" + data.message.id), data)
}

httpGetMessages.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    clearContents()

    var root = document.getElementById("contents")

    textAreaNode = document.createElement("textarea")
    textAreaNode.setAttribute("id", "newMessage")
    textAreaNode.setAttribute("rows", "4")
    textAreaNode.setAttribute("cols", "50")
    var newMessageColNode = divElementWithChild("col-sm-12", textAreaNode)
    var newMessageRowNode = divElementWithChild("row", newMessageColNode)

    var dbtn = document.createElement("input");
    dbtn.id = "publishButton"
    dbtn.setAttribute("type", "button")
    dbtn.setAttribute("value", "Julkaise uusi viesti")
    dbtn.setAttribute("onclick", "sendMessage()")
    var buttonColNode = divElementWithChild("col-sm-12", dbtn)
    var buttonRowNode = divElementWithChild("row", buttonColNode)

    root.appendChild(newMessageRowNode)
    root.appendChild(buttonRowNode)
    root.appendChild(emptyRow())

    var messageBlock = document.createElement("div")
    messageBlock.id = "messageBlock"
    root.appendChild(messageBlock)

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        messageBlock.appendChild(createMessageElement(data[i]))
    }
}

function messageTab() {
    highlightNavlink("messageTab")
    httpGetMessages.open("GET",contextRoot + "api/messages")
    httpGetMessages.send()
}

function likeMessage(id) {
    httpSendMessageLike.open("POST",contextRoot + "api/messages/" + id + "/likes")
    httpSendMessageLike.setRequestHeader("Content-type", "application/json");
    httpSendMessageLike.send() 
}

function addMessageComment(id) {
    var data = {comment: document.getElementById("comment" + id).value}
    httpSendMessageComment.open("POST",contextRoot + "api/messages/" + id + "/comments")
    httpSendMessageComment.setRequestHeader("Content-type", "application/json");
    httpSendMessageComment.send(JSON.stringify(data))  
    document.getElementById("comment" + id).value = ""
}

function sendMessage() {
    var data = {message: document.getElementById("newMessage").value}
    httpSendMessage.open("POST",contextRoot + "api/messages")
    httpSendMessage.setRequestHeader("Content-type", "application/json");
    httpSendMessage.send(JSON.stringify(data))  
    document.getElementById("newMessage").value = ""
}

function createMessageElement(data) {
    // Main container for message
    var mainColNode = divElement("col-sm-12")
    var mainRowNode = divElementWithChild("row", mainColNode)
    mainRowNode.className = "messageElement"

    // Full name row
    var nameRowNode = divElement("row")

    var name = document.createElement("h5")
    name.innerHTML = data.profile.fullname
    var nameNode = divElementWithChild("col-sm-4", name)

    var dateNode = divElementWithChild("col-sm-4", document.createTextNode(formatDate(data.date)))
    var likesText = document.createElement("div")
    likesText.innerHTML = "" + data.numberOfLikes + " tykkäystä"
    likesText.id = "likesNode" + data.id
    var likesNode = divElementWithChild("col-sm-2", likesText)

    var btn = document.createElement("input");
    btn.id = "likeButton"
    btn.setAttribute("type", "button")
    btn.setAttribute("value", "Tykkää")
    btn.setAttribute("onclick", "likeMessage("+ data.id + ")")
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
    messageNode.innerHTML = data.message
    messageColNode.appendChild(messageNode)
    mainColNode.appendChild(messageRowNode)

    // container for comments
    var commentRootRowNode = divElement("row")
    var commentColNode = divElement("col-sm-11")
    commentColNode.id = "commentsBlock" + data.id
    commentRootRowNode.appendChild(divElement("col-sm-1"))
    commentRootRowNode.appendChild(commentColNode)

    // add all comments
    if (!(data.messageComments == null)) {
        addComments(commentColNode, data.messageComments)
    }
    mainColNode.appendChild(commentRootRowNode)

    // new comment form
    var newCommentRow = divElement("row")
    var newCommentNode = document.createElement("textarea")
    newCommentNode.setAttribute("id", "comment" + data.id)
    newCommentNode.setAttribute("rows", "1")
    newCommentNode.setAttribute("cols", "30")
    newCommentRow.appendChild(newCommentNode)
    var cbtn = document.createElement("input");
    cbtn.id = "commentButton"
    cbtn.setAttribute("type", "button")
    cbtn.setAttribute("value", "Lähetä kommentti")
    cbtn.setAttribute("onclick", "addMessageComment("+ data.id + ")")
    newCommentRow.appendChild(cbtn)

    var newCommentRowBlock = divElementWithChild("row", divElement("col-sm-1"))
    newCommentRowBlock.appendChild(divElementWithChild("col-sm-11", newCommentRow))
    mainColNode.appendChild(newCommentRowBlock)

    return mainRowNode
}