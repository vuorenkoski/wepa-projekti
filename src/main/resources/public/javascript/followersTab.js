var httpGetFollowers = new XMLHttpRequest()
var httpGetFollow = new XMLHttpRequest()
var httpSendFollowers = new XMLHttpRequest()

function followersTab() {
    highlightNavlink("followersTab")
    httpGetFollowers.open("GET",contextRoot + "followers")
    httpGetFollowers.send()
    httpGetFollow.open("GET",contextRoot + "follow")
    httpGetFollow.send()
}

httpGetFollow.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    var root = document.getElementById("contents1")

    while (root.firstChild) {
      root.removeChild(root.lastChild);
    }

    var followTitle = document.createElement("h5")
    followTitle.innerHTML = "Seurattavat käyttäjät"
    root.appendChild(divElementWithChild("row", followTitle))

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
        root.appendChild(userRow)
    }

    var input = document.createElement("input")
    input.setAttribute("type", "text");
    input.setAttribute("placeholder", "profiilin tunnus");
    input.id = "profileName"
    row = divElementWithChild("row", input)
    var addButton = document.createElement("input");
    addButton.setAttribute("type", "button")
    addButton.setAttribute("value", "Lisää seurattava")
    addButton.setAttribute("onclick", "follow()")
    row.appendChild(addButton)
    root.appendChild(row)
}

httpGetFollowers.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }

    var root = document.getElementById("contents2")

    while (root.firstChild) {
      root.removeChild(root.lastChild);
    }

    root.appendChild(emptyRow())

    var followTitle = document.createElement("h5")
    followTitle.innerHTML = "Seuraajat"
    root.appendChild(divElementWithChild("row", followTitle))

    var data = JSON.parse(this.responseText)
    for (i=0; i<data.length; i++) {
        console.log(data[i].hidden)
        if (data[i].hidden) {
            var user = document.createElement("del")
            user.innerHTML = data[i].profile.fullname + " (aloittanut" + formatDate(data[i].date) + ")&nbsp;&nbsp;" 
            var userRow = divElementWithChild("row", user)
            var remove = document.createElement("div")
            remove.innerHTML = "poista esto"
            remove.classList.add("removeColor")
            remove.classList.add("mousePointer")
            remove.setAttribute("onclick", "unhideFollower(" + data[i].id + ")")
            userRow.appendChild(remove)
            root.appendChild(userRow) 
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
            root.appendChild(userRow)
        }
    }

    root.appendChild(emptyRow())
}

function follow() {
    var data = {profilename: document.getElementById("profileName").value}
    httpSendFollowers.open("POST",contextRoot + "follow")
    httpSendFollowers.setRequestHeader("Content-type", "application/json");
    httpSendFollowers.send(JSON.stringify(data))  
    document.getElementById("profileName").value = ""
} 

httpSendFollowers.onreadystatechange = function() {
    if (this.readyState!=4 || this.status!=200) {
        return
    }
    httpGetFollow.open("GET",contextRoot + "follow")
    httpGetFollow.send()
    httpGetFollowers.open("GET",contextRoot + "followers")
    httpGetFollowers.send()
}

function removeFollow(id) {
    httpSendFollowers.open("DELETE",contextRoot + "follow/" + id)
    httpSendFollowers.send()  
}

function hideFollower(id) {
    httpSendFollowers.open("POST",contextRoot + "follower/" + id + "/hide")
    httpSendFollowers.send()  
}

function unhideFollower(id) {
    httpSendFollowers.open("POST",contextRoot + "follower/" + id + "/unhide")
    httpSendFollowers.send()  
}