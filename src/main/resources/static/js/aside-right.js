"use strict"

function countryClicked(id){
	if(document.getElementById("list"+id).style.display==="none"){
		document.getElementById("list"+id).style.display="block";
		document.getElementById(id).innerText="" + id + "▽";
	}else{
		document.getElementById("list"+id).style.display="none";
		document.getElementById(id).innerText="" + id + "▷";
	}
}
