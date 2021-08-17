var httpGetFollowers = new XMLHttpRequest()
var httpGetFollow = new XMLHttpRequest()
var httpGetProfiles = new XMLHttpRequest()
var httpSendFollowers = new XMLHttpRequest()

var followRoot = document.createElement("div")
var profilesRoot = document.createElement("div")
var followerRoot = document.createElement("div")

function followersTab() {
    highlightNavlink("followersTab")

    var root = document.getElementById("contents1")
    var rootRow = document.createElement("div")
    rootRow.classList.add("row")
    var followColumn = document.createElement("div")
    followColumn.classList.add("col-sm-6")

    followColumn.appendChild(divElementWithChild("row",followRoot))
    followColumn.appendChild(divElementWithChild("row",profilesRoot))

    followRoot.classList.add("col-sm-12")
    profilesRoot.classList.add("col-sm-12")
    followerRoot.classList.add("col-sm-6")

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

    while (followRoot.firstChild) {
      followRoot.removeChild(followRoot.lastChild);
    }

    var followTitle = document.createElement("h4")
    followTitle.innerHTML = "Seurattavat käyttäjät"
    followRoot.appendChild(divElementWithChild("row", followTitle))

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
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
    followRoot.appendChild(emptyRow())

    var input = document.createElement("input")
    input.setAttribute("type", "text");
    input.setAttribute("placeholder", "profiilin tunnus");
    input.id = "profileName"
    row = divElementWithChild("row", input)
    var addButton = document.createElement("input");
    addButton.setAttribute("type", "button")
    addButton.setAttribute("value", "Hae seurattava")
    addButton.setAttribute("onclick", "getProfiles()")
    row.appendChild(addButton)
    followRoot.appendChild(row)
    followRoot.appendChild(emptyRow())
}

httpGetProfiles.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    while (profilesRoot.firstChild) {
        profilesRoot.removeChild(profilesRoot.lastChild);
    }

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
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
        if (data[i].hidden) {
            var user = document.createElement("del")
            user.innerHTML = data[i].profile.fullname + " (aloittanut " + formatDate(data[i].date) + ")&nbsp;&nbsp;" 
            var userRow = divElementWithChild("row", user)
            var remove = document.createElement("div")
            remove.innerHTML = "poista esto"
            remove.classList.add("removeColor")
            remove.classList.add("mousePointer")
            remove.setAttribute("onclick", "unhideFollower(" + data[i].id + ")")
            userRow.appendChild(remove)
            followerRoot.appendChild(userRow) 
        } else {
            var user = document.createElement("div")
            user.innerHTML = data[i].profile.fullname + " (aloittanut " + formatDate(data[i].date) + ")&nbsp;&nbsp;" 
            var userRow = divElementWithChild("row", user)
            var remove = document.createElement("div")
            remove.innerHTML = "estä"
            remove.classList.add("removeColor")
            remove.classList.add("mousePointer")
            remove.setAttribute("onclick", "hideFollower(" + data[i].id + ")")
            userRow.appendChild(remove)
            followerRoot.appendChild(userRow)
        }
    }

    followerRoot.appendChild(emptyRow())
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