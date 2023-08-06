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