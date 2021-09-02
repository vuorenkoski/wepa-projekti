var httpDeletePhoto = new XMLHttpRequest()
var httpSendPhotoLike = new XMLHttpRequest()
var httpSendPhotoComment = new XMLHttpRequest()
var httpChangeProfile = new XMLHttpRequest()
var httpSendPhoto = new XMLHttpRequest()

httpDeletePhoto.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGetPhotos.open("GET",contextRoot + "api/photos")
    httpGetPhotos.send()
}

httpSendPhoto.onreadystatechange = function() {
    if (this.readyState!=4) {
        return
    }
    if  (this.status!=201) {
        var data = JSON.parse(this.responseText)
        alert(data.message)
        document.getElementById("loadMessage").innerHTML = ""
        return
    }
    document.getElementById("loadMessage").innerHTML = ""
    var data = JSON.parse(this.responseText)
    document.getElementById("photoBlock").prepend(createPhotoElement(data))
}

httpSendPhotoComment.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    var data = JSON.parse(this.responseText)
    addComment(document.getElementById("commentsBlock" + data.photo.id), data)
}

httpSendPhotoLike.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    var photo = JSON.parse(this.responseText)
    document.getElementById("likesNode" + photo.id).innerHTML = "" + photo.numberOfLikes + " tykkäystä"
}

httpChangeProfile.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    var id = JSON.parse(httpChangeProfile.response).photo_id
    document.getElementById("profilePhoto").src=contextRoot + "api/photos/" + id + "?height=150"
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
    var loadMessage = document.createElement("b")
    loadMessage.id = "loadMessage"
    var p = document.createElement("p")
    p.appendChild(addButtonNode)
    p.appendChild(loadMessage)
    addFormNode.appendChild(p)

    addFormNode.addEventListener("submit", e => {
        e.preventDefault()
        const files = document.querySelector("[name=image]").files
        const formData = new FormData()
        formData.append("image", files[0])
        formData.append("description", document.querySelector("[name=description]").value)
        document.querySelector("[name=image]").value = ""
        document.querySelector("[name=description]").value = ""
        document.getElementById("loadMessage").innerHTML = "Ladataan..."
        httpSendPhoto.open("POST", contextRoot + "api/photos")
        httpSendPhoto.send(formData)
    });
  
    var newPhotoRowNode = divElementWithChild("row", divElementWithChild("col-sm-12", addFormNode))

    root.appendChild(newPhotoRowNode)
    root.appendChild(emptyRow())

    var photoBlock = document.createElement("div")
    photoBlock.id = "photoBlock"
    root.appendChild(photoBlock)

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        photoBlock.appendChild(createPhotoElement(data[i]))
    }
}

function photoTab() {
    highlightNavlink("photoTab")
    httpGetPhotos.open("GET",contextRoot + "api/photos")
    httpGetPhotos.send()
}

function likePhoto(id) {
    httpSendPhotoLike.open("POST",contextRoot + "api/photos/" + id + "/likes")
    httpSendPhotoLike.setRequestHeader("Content-type", "application/json");
    httpSendPhotoLike.send() 
}

function profilePhoto(id) {
    httpChangeProfile.open("POST",contextRoot + "profile/photo/?id=" + id)
    httpChangeProfile.setRequestHeader("Content-type", "application/json");
    httpChangeProfile.send() 
}

function deletePhoto(id) {
    httpDeletePhoto.open("DELETE",contextRoot + "api/photos/" + id)
    httpDeletePhoto.setRequestHeader("Content-type", "application/json");
    httpDeletePhoto.send() 
}

function addPhotoComment(id) {
    var data = {comment: document.getElementById("comment" + id).value}
    httpSendPhotoComment.open("POST",contextRoot + "api/photos/" + id + "/comments")
    httpSendPhotoComment.setRequestHeader("Content-type", "application/json");
    httpSendPhotoComment.send(JSON.stringify(data))  
    document.getElementById("comment" + id).value = ""
}

function createPhotoElement(data) {
    // Main container for photo
    var mainColNode = divElement("col-sm-12")
    var mainRowNode = divElementWithChild("row", mainColNode)

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
    btn.setAttribute("type", "button")
    btn.setAttribute("value", "Tykkää")
    btn.setAttribute("onclick", "likePhoto("+ data.id + ")")
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
    photoNode.setAttribute("src", contextRoot + "api/photos/" + data.id)
    var photoLinkNode = document.createElement("a")
    photoLinkNode.href = contextRoot + "api/photos/" + data.id
    photoLinkNode.appendChild(photoNode)
    photoColNode.appendChild(photoLinkNode)
    mainColNode.appendChild(photoRowNode)

    // Description row
    var descriptionColNode = divElement("col-sm-12")
    var descriptionRowNode = divElementWithChild("row", descriptionColNode)
    var descriptionNode = document.createElement("p")
    descriptionNode.innerHTML = data.description
    descriptionColNode.appendChild(descriptionNode)
    mainColNode.appendChild(descriptionRowNode)

    // Deletebutton
    if (userId == data.profile.id) {
        var deleteColNode = divElement("col-sm-3")
        var profileColNode = divElement("col-sm-3")
        var deleteRowNode = divElementWithChild("row", deleteColNode)
        deleteRowNode.appendChild(profileColNode)

        var deleteNode = document.createElement("input");
        deleteNode.setAttribute("type", "button")
        deleteNode.setAttribute("value", "Poista kuva")
        deleteNode.setAttribute("onclick", "deletePhoto("+ data.id + ")")
        deleteColNode.appendChild(deleteNode)

        var profileNode = document.createElement("input");
        profileNode.setAttribute("type", "button")
        profileNode.setAttribute("value", "aseta profiilikuvaksi")
        profileNode.setAttribute("onclick", "profilePhoto("+ data.id + ")")
        profileColNode.appendChild(profileNode)

        mainColNode.appendChild(deleteRowNode)
        mainColNode.appendChild(emptyRow())           

    }

    // container for comments
    var commentRootRowNode = divElement("row")
    var commentColNode = divElement("col-sm-11")
    commentColNode.id = "commentsBlock" + data.id

    commentRootRowNode.appendChild(divElement("col-sm-1"))
    commentRootRowNode.appendChild(commentColNode)

    if (!(data.photoComments == null)) {
        addComments(commentColNode, data.photoComments)
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
    cbtn.setAttribute("type", "button")
    cbtn.setAttribute("value", "Lähetä kommentti")
    cbtn.setAttribute("onclick", "addPhotoComment("+ data.id + ")")
    newCommentRow.appendChild(cbtn)

    var newCommentRowBlock = divElementWithChild("row", divElement("col-sm-1"))
    newCommentRowBlock.appendChild(divElementWithChild("col-sm-11", newCommentRow))
    mainColNode.appendChild(newCommentRowBlock)

    // extra empty rows at the end
    mainColNode.appendChild(emptyRow())
    mainColNode.appendChild(emptyRow())

    return mainRowNode
}