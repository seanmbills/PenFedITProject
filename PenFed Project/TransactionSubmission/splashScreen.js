
var rootRef = firebase.database().ref().child("Users");

var enteredEmail = document.getElementById("emailEntry").value;

rootRef.on("child_added", snap => {
    var firstName = snap.child("firstName").val();
    var email = snap.child("email").val();
    checkEmails(email, enteredEmail);
});

function checkEmails(email, enteredEmail) {
if (email === enteredEmail) {
        window.alert(firstName + email);
    }
}