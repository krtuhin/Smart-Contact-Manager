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