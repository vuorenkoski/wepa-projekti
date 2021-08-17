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

    var newMessageRowNode = document.createElement("div")
    newMessageRowNode.classList.add("row")
    var newMessageColNode = document.createElement("div")
    newMessageColNode.classList.add("col-sm-12")
    newMessageRowNode.appendChild(newMessageColNode)
    var textAreaRowNode = document.createElement("div")
    textAreaRowNode.classList.add("row")
    textAreaNode = document.createElement("textarea")
    textAreaNode.setAttribute("id", "newMessage")
    textAreaNode.setAttribute("rows", "4")
    textAreaNode.setAttribute("cols", "50")
    newMessageColNode.appendChild(textAreaNode)

    var buttonRowNode = document.createElement("div")
    buttonRowNode.classList.add("row")
    var buttonColNode = document.createElement("div")
    buttonColNode.classList.add("col-sm-12")
    buttonRowNode.appendChild(buttonColNode)
    var dbtn = document.createElement("input");
    dbtn.setAttribute("type", "button")
    dbtn.setAttribute("value", "Julkaise uusi viesti")
    dbtn.setAttribute("onclick", "sendMessage()")
    buttonColNode.appendChild(dbtn)

    var emptyRowNode = document.createElement("div")
    emptyRowNode.classList.add("row")
    emptyRowNode.appendChild(document.createElement("br"))

    root.appendChild(newMessageRowNode)
    root.appendChild(buttonRowNode)
    root.appendChild(emptyRowNode)

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
        // Main container for message
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
        likesNode.appendChild(document.createTextNode("" + data[i].numberOfLikes + " tykkäystä"))
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
