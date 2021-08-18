var httpGetFollowers = new XMLHttpRequest()
var httpGetFollow = new XMLHttpRequest()
var httpGetProfiles = new XMLHttpRequest()
var httpSendFollowers = new XMLHttpRequest()

var followRoot = divElement("col-sm-12")
var profilesRoot = divElement("col-sm-12")
var followerRoot = divElement("col-sm-6")

function followersTab() {
    highlightNavlink("followersTab")

    var root = document.getElementById("contents1")
    var rootRow = divElement("row")
    var followColumn = divElement("col-sm-6")

    followColumn.appendChild(divElementWithChild("row",followRoot))
    followColumn.appendChild(emptyRow())
    followColumn.appendChild(searchElement())
    followColumn.appendChild(emptyRow())
    followColumn.appendChild(divElementWithChild("row",profilesRoot))
    rootRow.appendChild(followColumn)
    rootRow.appendChild(followerRoot)
    root.appendChild(rootRow)

    httpGetFollowers.open("GET",contextRoot + "api/followers")
    httpGetFollowers.send()
    httpGetFollow.open("GET",contextRoot + "api/follow")
    httpGetFollow.send()
}

httpGetFollow.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    clearNode(followRoot)

    var followTitle = document.createElement("h4")
    followTitle.innerHTML = "Seurattavat käyttäjät"
    followRoot.appendChild(divElementWithChild("row", followTitle))

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        var user = document.createElement("div")
        user.innerHTML = data[i].follow.fullname + "&nbsp;&nbsp;--"
        userRow = divElementWithChild("row", user)
        var remove = document.createElement("div")
        remove.innerHTML = "poista" 
        remove.classList.add("removeColor")
        remove.classList.add("mousePointer")
        remove.setAttribute("onclick", "removeFollow(" + data[i].id + ")")
        userRow.appendChild(remove)
        followRoot.appendChild(userRow)
    }
}

httpGetProfiles.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    while (profilesRoot.firstChild) {
        profilesRoot.removeChild(profilesRoot.lastChild);
    }

    var data = JSON.parse(this.responseText)
    for (var i = 0; i < data.length; i++) {
        var user = document.createElement("div")
        user.innerHTML = data[i].fullname + "&nbsp;&nbsp;--"
        userRow = divElementWithChild("row", user)
        var remove = document.createElement("div")
        remove.innerHTML = "lisaa" 
        remove.classList.add("addColor")
        remove.classList.add("mousePointer")
        remove.setAttribute("onclick", "follow(" + data[i].id + ")")
        userRow.appendChild(remove)
        profilesRoot.appendChild(userRow)
    }
    profilesRoot.appendChild(emptyRow())
}

httpGetFollowers.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    while (followerRoot.firstChild) {
      followerRoot.removeChild(followerRoot.lastChild);
    }

    var followTitle = document.createElement("h4")
    followTitle.innerHTML = "Seuraajat"
    followerRoot.appendChild(divElementWithChild("row", followTitle))

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
        var remove = document.createElement("div")
        if (data[i].hidden) {
            var user = document.createElement("del")
            remove.innerHTML = "poista esto"
            remove.setAttribute("onclick", "unhideFollower(" + data[i].id + ")")
        } else {
            var user = document.createElement("div")
            remove.innerHTML = "estä"
            remove.setAttribute("onclick", "hideFollower(" + data[i].id + ")")
        }
        user.innerHTML = data[i].profile.fullname + " (aloittanut " + formatDate(data[i].date) + ")&nbsp;&nbsp;" 
        var userRow = divElementWithChild("row", user)
        remove.classList.add("removeColor")
        remove.classList.add("mousePointer")
        userRow.appendChild(remove)
        followerRoot.appendChild(userRow)
    }

    followerRoot.appendChild(emptyRow())
}

function searchElement() {
    var input = document.createElement("input")
    input.setAttribute("type", "text");
    input.setAttribute("placeholder", "käyttäjän nimi");
    input.id = "profileName"
    var row = divElementWithChild("row", input)
    var addButton = document.createElement("input");
    addButton.setAttribute("type", "button")
    addButton.setAttribute("value", "Hae seurattavia")
    addButton.setAttribute("onclick", "getProfiles()")
    row.appendChild(addButton)
    return row
}

function getProfiles() {
    httpGetProfiles.open("GET",contextRoot + "api/profiles?name=" + document.getElementById("profileName").value)
    httpGetProfiles.send()
}

function follow(id) {
    httpSendFollowers.open("POST",contextRoot + "api/follow/" + id)
    httpSendFollowers.send()
} 

httpSendFollowers.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGetFollow.open("GET",contextRoot + "api/follow")
    httpGetFollow.send()
    httpGetFollowers.open("GET",contextRoot + "api/followers")
    httpGetFollowers.send()
}

function removeFollow(id) {
    httpSendFollowers.open("DELETE",contextRoot + "api/follow/" + id)
    httpSendFollowers.send()  
}

function hideFollower(id) {
    httpSendFollowers.open("POST",contextRoot + "api/follower/" + id + "/hide")
    httpSendFollowers.send()  
}

function unhideFollower(id) {
    httpSendFollowers.open("POST",contextRoot + "api/follower/" + id + "/unhide")
    httpSendFollowers.send()  
}