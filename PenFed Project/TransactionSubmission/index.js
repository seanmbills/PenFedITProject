$(document).ready(function() {
    var rootRef = firebase.database().ref().child("Users");

    rootRef.on("child_added", snap => {
        var name = snap.child("firstName").val();
        var email = snap.child("email").val();
        var password = snap.child("password").val();
        var school = snap.child("schoolName").val();
        var userId = snap.child("accId").val();

        //alert(name + "\n" + email + "\n" + password + "\n" + school);

        var pending = rootRef.child(userId).child("pendingTransactions");

        pending.on("child_added", snapshot => {
            var transactionAmount = snapshot.child("transactionAmount").val();
            var status = snapshot.child("status").val();
            var transactionDate = snapshot.child("transactionDate").val();
            var business = snapshot.child("businessEndpoint").val();

            // alert(transactionAmount + "\n" + status + "\n"
            //     + transactionDate + "\n" + business + "\n");

            $("#table_body").append("<tr><td>" + transactionDate
                + "</td><td>" + email + "</td><td>" + transactionAmount
                + "</td><td>" + business + "</td><td>" + status
                + "</td><td>" + "<button>Modify</button>" + "</td></tr>");
        });

        var completed = rootRef.child(userId).child("completedTransactions");

        completed.on("child_added", snapshot => {
            var transactionAmount = snapshot.child("transactionAmount").val();
            var status = snapshot.child("status").val();
            var transactionDate = snapshot.child("transactionDate").val();
            var business = snapshot.child("businessEndpoint").val();

            // alert(transactionAmount + "\n" + status + "\n"
            //     + transactionDate + "\n" + business + "\n");

            $("#table_body").append("<tr><td>" + transactionDate
                + "</td><td>" + email + "</td><td>" + transactionAmount
                + "</td><td>" + business + "</td><td>" + status + "</td><tr>");
        });
    });
});