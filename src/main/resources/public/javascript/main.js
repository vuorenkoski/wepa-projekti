function highlightNavlink(link) {
    links = document.querySelectorAll("nav a")
    for (var i = 0; i < links.length; i++) {
//        links[i].classname = "nav-link"
        if (links[i].id == link) {
            links[i].classList.add("active")
        } else {
            links[i].classList.remove("active")
        }
    }
}

messageTab()

