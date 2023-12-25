"use strict"

function countryClicked(id) {
    var listElement = document.getElementById("list" + id);

    if (listElement.style.display === "none") {
        listElement.style.display = "block";
        document.getElementById(id).innerText = "" + id + "▽";
    } else {
        listElement.style.display = "none";
        document.getElementById(id).innerText = "" + id + "▷";
    }
}
