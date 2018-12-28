// get modal element
var modal = document.getElementById('simpleModal');
// get open modal buttom
var modalBtn = document.getElementById('modalBtn');
// get close btn
var closeBtn = document.getElementsByClassName('closeBtn')[0];

//listen for click
modalBtn.addEventListener('click', openModal);
closeBtn.addEventListener('click', closeModal);
//listen for outside click
window.addEventListener('click', clickOutside)

//function to open and close model
function openModal() {
	modal.style.display = 'block';
}

function closeModal() {
	modal.style.display = 'none';
}

function clickOutside(e) {
	if(e.target == modal) {
		modal.style.display = 'none';	
	}
}