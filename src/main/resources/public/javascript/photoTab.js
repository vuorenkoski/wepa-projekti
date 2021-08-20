var httpSendPhotos = new XMLHttpRequest()

httpSendPhotos.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGetPhotos.open("GET",contextRoot + "api/photos")
    httpGetPhotos.send()
}

var httpGetPhotos = new XMLHttpRequest()

httpGetPhotos.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    clearContents()

    var root = document.getElementById("contents1")

    var addFormNode = document.createElement("form")
    addFormNode.setAttribute("action", contextRoot + "api/photos")
    addFormNode.setAttribute("method", "POST")
    addFormNode.setAttribute("enctype", "multipart/form-data")

    var addImageNode = document.createElement("input")
    addImageNode.setAttribute("type", "file")
    addImageNode.setAttribute("name", "image")
    var p = document.createElement("p")
    p.appendChild(addImageNode)
    addFormNode.appendChild(p)

    var addDescriptionNode = document.createElement("input")
    addDescriptionNode.setAttribute("type", "text")
    addDescriptionNode.setAttribute("name", "description")
    var p = document.createElement("p")
    p.appendChild(addDescriptionNode)
    addFormNode.appendChild(p)

    var addButtonNode = document.createElement("input");
    addButtonNode.setAttribute("type", "submit")
    addButtonNode.setAttribute("value", "Julkaise uusi kuva")
    var p = document.createElement("p")
    p.appendChild(addButtonNode)
    addFormNode.appendChild(p)

//    addFormNode.submit(function(e){
//        e.preventDefault()
//        $.ajax({
//            url: contextRoot + "api/photos",
//           type: "post",
//            data:addFormNode.serialize(),
//            success:function(){
//                // Whatever you want to do after the form is successfully submitted
//            }
//        });
//    });
    

    var newPhotoRowNode = divElementWithChild("row", divElementWithChild("col-sm-12", addFormNode))

    root.appendChild(newPhotoRowNode)
    root.appendChild(emptyRow())

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        // Main container for photo
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
        btn.setAttribute("onclick", "likePhoto("+ data[i].id + ")")
        var buttonNode = divElementWithChild("col-sm-2", btn)

        nameRowNode.appendChild(nameNode)
        nameRowNode.appendChild(dateNode)
        nameRowNode.appendChild(likesNode)
        nameRowNode.appendChild(buttonNode)
        mainColNode.appendChild(nameRowNode)

        // Photo row
        var photoColNode = divElement("col-sm-12")
        var photoRowNode = divElementWithChild("row", photoColNode)
        var photoNode = document.createElement("p")
        photoNode.innerHTML = data[i].photo
        photoColNode.appendChild(photoNode)
        mainColNode.appendChild(photoRowNode)

        // container for comments
        var commentRootRowNode = divElement("row")
        var commentColNode = divElement("col-sm-11")
        commentRootRowNode.appendChild(divElement("col-sm-1"))
        commentRootRowNode.appendChild(commentColNode)

        // add all comments
        addComments(commentColNode, data[i].photoComments)

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

        var photoNode = document.createElement("p")
        photoNode.innerHTML = commentdata[i].comment
        var photoColNode = divElementWithChild("col-sm-12", photoNode)
        var photoRowNode = divElementWithChild("row", photoColNode)
        root.appendChild(photoRowNode)
    }
}

function photoTab() {
    highlightNavlink("photoTab")
    httpGetPhotos.open("GET",contextRoot + "api/photos")
    httpGetPhotos.send()
}

function likePhoto(id) {
    console.log("tykkäys: " + id)
    httpSendPhotos.open("POST",contextRoot + "api/photos/" + id + "/likes")
    httpSendPhotos.setRequestHeader("Content-type", "application/json");
    httpSendPhotos.send() 
}

function addPhotoComment(id) {
    var data = {comment: document.getElementById("comment" + id).value}
    httpSendPhotos.open("POST",contextRoot + "api/photos/" + id + "/comments")
    httpSendPhotos.setRequestHeader("Content-type", "application/json");
    httpSendPhotos.send(JSON.stringify(data))  
    document.getElementById("comment" + id).value = ""
}

