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

    addFormNode.addEventListener("submit", e => {
        e.preventDefault()
        const files = document.querySelector("[name=image]").files
        const formData = new FormData()
        formData.append("image", files[0])
        formData.append("description", document.querySelector("[name=description]").value)
        const xhr = new XMLHttpRequest()
        xhr.onload = () => { photoTab() }
        xhr.open("POST", contextRoot + "api/photos")
        xhr.send(formData)
    });
  
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

        // photo row
        var photoColNode = divElement("col-sm-12")
        var photoRowNode = divElementWithChild("row", photoColNode)
        var photoNode = document.createElement("img")
        photoNode.setAttribute("src", contextRoot + "api/photos/" + data[i].id)
        photoNode.setAttribute("width", "500")
        var photoLinkNode = document.createElement("a")
        photoLinkNode.href = contextRoot + "api/photos/" + data[i].id
        photoLinkNode.appendChild(photoNode)
        photoColNode.appendChild(photoLinkNode)
        mainColNode.appendChild(photoRowNode)

        // Description row
        var descriptionColNode = divElement("col-sm-12")
        var descriptionRowNode = divElementWithChild("row", descriptionColNode)
        var descriptionNode = document.createElement("p")
        descriptionNode.innerHTML = data[i].description
        descriptionColNode.appendChild(descriptionNode)
        mainColNode.appendChild(descriptionRowNode)

        // Deletebutton
        if (userId == data[i].profile.id) {
            var deleteColNode = divElement("col-sm-12")
            var deleteRowNode = divElementWithChild("row", deleteColNode)

            var deleteNode = document.createElement("input");
            deleteNode.setAttribute("type", "button")
            deleteNode.setAttribute("value", "Poista kuva")
            deleteNode.setAttribute("onclick", "deletePhoto("+ data[i].id + ")")
            deleteColNode.appendChild(deleteNode)
            mainColNode.appendChild(deleteRowNode)
        }

        // container for comments
        var commentRootRowNode = divElement("row")
        var commentColNode = divElement("col-sm-11")
        commentColNode.setAttribute("id", "comments" + data[i].id)

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
        cbtn.setAttribute("onclick", "addPhotoComment("+ data[i].id + ")")
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

function photoTab() {
    highlightNavlink("photoTab")
    httpGetPhotos.open("GET",contextRoot + "api/photos")
    httpGetPhotos.send()
}

function likePhoto(id) {
    httpSendPhotos.open("POST",contextRoot + "api/photos/" + id + "/likes")
    httpSendPhotos.setRequestHeader("Content-type", "application/json");
    httpSendPhotos.send() 
}

function deletePhoto(id) {
    httpSendPhotos.open("DELETE",contextRoot + "api/photos/" + id)
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

