
    // rootRef.on("child_added", snap => {
    //     var name = snap.child("firstName").val();
    //     var userEmail = snap.child("email").val();
    //     var password = snap.child("password").val();
    //     var school = snap.child("schoolName").val();
    //     var userId = snap.child("accId").val();

    // rootRef.once("value").then(function(snapshot) {
    //     snapshot.forEach(function(childSnapshot) {
    //         var hasEmail = childSnapshot.hasChild("email");
    //         alert("Has Email: " + hasEmail);
    //         var child = childSnapshot.child("email").val();
    //         alert(child);
    //     })
    // })

    //     alert(name + userEmail + password + school + userId + "\nDocument ready...");

    //     // if (email === userEmail) {
    //     //     var pending = rootRef.child(userId).child("pendingTransactions");
    //     //     var transaction = {transactionAmount:parseFloat(amount), status:transactionStatus, businessEndpoint: business, transactionDate: date};

    //     //     pending.push().setValue(transaction);
    //     // }
    //     //alert(name + "\n" + email + "\n" + password + "\n" + school);
    // });

function submitTransaction() {

    var currDate = new Date();
    var date = currDate.getFullYear() + "/" + (currDate.getMonth()+1)
        + "/" + currDate.getDate() + " " + currDate.getHours()
        + ":" + currDate.getMinutes() + ":" + currDate.getSeconds();
    var email = document.getElementById("emailEntry").value;
    var amount = document.getElementById("transactionAmountEntry").value;
    var transactionStatus = "PENDING";
    var business = document.getElementById("businessEntry").value;

    // rootRef.once("value").then(function(snapshot) {
    //     window.alert(snapshot.val());
    //     snapshot.forEach(function(childSnapshot) {
    //         var key = childSnapshot.key;
    //         alert("Key: " + key);
    //         var hasEmail = childSnapshot.hasChild("email");
    //         alert(hasEmail);
    //         var childEmail = childSnapshot.val();
    //         alert(childEmail);
    //     });
    // });

    var rootRef = firebase.database().ref().child("Users");

    rootRef.once("value").then(function(snapshot) {
        snapshot.forEach(function(childSnapshot) {
            var hasEmail = childSnapshot.hasChild("email");
            alert("Has Email: " + hasEmail);
            var child = childSnapshot.child("email").val();
            alert(child);
        })
    })

    // rootRef.on("child_added", snap => {
    //     var name = snap.child("firstName").val();
    //     var userEmail = snap.child("email").val();
    //     var password = snap.child("password").val();
    //     var school = snap.child("schoolName").val();
    //     var userId = snap.child("accId").val();

    //     alert(name + userEmail + password + school + userId);

    //     if (email === userEmail) {
    //         var pending = rootRef.child(userId).child("pendingTransactions");
    //         var transaction = {transactionAmount:parseFloat(amount), status:transactionStatus, businessEndpoint: business, transactionDate: date};

    //         pending.push().setValue(transaction);
    //     }
    //     //alert(name + "\n" + email + "\n" + password + "\n" + school);
    // })

    window.alert(email + "\n" + amount + "\n" + date
        + "\n" + transactionStatus + "\n" + business + "\nsubmitting Transaction...");

    window.location.href='./index.html';

}