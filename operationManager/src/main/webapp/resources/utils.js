function clearField(obj) {
	obj.value = "";
}

function restoreDefaultIfEmpty(obj, defaultString) {
	obj.value = (obj.value == "") ? defaultString : obj.value;
}