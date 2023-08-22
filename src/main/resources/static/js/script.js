// sidebar show and hide
const toggleSidebar = () => {

    if ($(".sidebar").is(":visible")) {

        // hide
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
        $(".barBtn").css("display", "block");

    } else {

        // show
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
        $(".barBtn").css("display", "none");

    }
};

//method for search contact
const search = () => {

    //getting input value
    let query = $("#search-input").val();

    //if input value not blank
    if (query.trim() != "") {

        //sending request to server
        let url = "http://127.0.0.1:8080/search/" + query;

        //fetch result
        fetch(url).then((response) => {

            //return response as json
            return response.json();

        }).then((data) => {

            //send data as html
            let text = `<div class="list-group">`;

            //fetching all the contact into html data
            data.forEach(contact => {

                text += `<a href="/user/contact/${contact.id}" 
                            class="list-group-item list-group-action"> ${contact.name} </a>`

            });

            text += `</div>`;
            //end html data

            //send html to view
            $(".search-result").html(text);

            //show result view
            $(".search-result").show();
        });

    } else {

        //if input value contain blank string
        $(".search-result").hide();
    }
};

//first request to server to create order
const paymentStart = () => {

    //fetch amount value from input
    let amount = $("#amount").val();

    //if amount empty
    if (amount == " " || amount == null) {

        alert("required field");

        return;
    }

    //ajax function to send request to server to create order
    $.ajax({
        url: "/user/create-order",
        data: JSON.stringify({amount: amount, info: "payment_order"}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {

            console.log(response);

            if (response.status == 'created') {

                //open payment form
                let options = {
                    key: "rzp_test_JZAipOiOr7CDus",
                    amount: response.amount,
                    currency: "INR",
                    name: "Smart Contact Manager",
                    description: "Donation",
                    image: "",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log("payment successful");

                        //update status in database
                        updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");

                    },
                    "prefill": {
                        "name": "",
                        "email": "",
                        "contact": ""
                    },
                    "notes": {
                        "address": "Tuhin Kumar Mandal"
                    },
                    "theme": {
                        "color": "#3399cc"
                    }
                };

                // razorpay object
                let rzp = new Razorpay(options);

                //if payment failed
                rzp.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.reason);
                    console.log(response.error.step);
                    console.log(response.error.metadata.payment_id);
                    console.log(response.error.metadata.order_id);
                    swal("Oops!", "Payment failed..!", "error");
                });

                //open form
                rzp.open();
            }
        },

        //if occurred any error
        error: function (error) {

            swal("Oops!", "Something went wrong..!", "error");

            console.log("error : " + error);
        }
    });
};

//method for update payment on server
updatePaymentOnServer = (payment_id, order_id, status) => {

    $.ajax({
        url: "/user/update-payment",
        data: JSON.stringify({payment_id: payment_id, order_id: order_id, status: status}),
        contentType: "application/json",
        type: "POST",
        dataType: "json",
        success: function (response) {

            swal("Congratulations!", "Payment successful..!", "success");

        },
        error: function (error) {

            swal("Congratulations!", "Payment successful, but we couldn't capture, we will contact you ASAP!", "warning");

        },
    });
};