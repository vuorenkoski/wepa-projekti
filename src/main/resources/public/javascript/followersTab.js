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
       user.innerHTML = data[i].follow.fullname
       root.appendChild(divElementWithChild("row", user))
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
        var user = document.createElement("div")
        user.innerHTML = data[i].profile.fullname
        var userRow = divElementWithChild("row", user)
        root.appendChild(userRow)
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
}