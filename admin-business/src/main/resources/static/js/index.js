$(document).ready(function () {
    //Toggle the left navigation expand/collapse icon
    $(".nav-sidebar-head .nav li a").click(function () {
        $(this).children().toggleClass("glyphicon-plus glyphicon-minus");
    });

    var sidebarBodyLi = $(".nav-sidebar-body .nav li");
    sidebarBodyLi.click(function () {
        //Update the selected left-navigation item
        sidebarBodyLi.not(this).removeClass("active");
    });

    // Create the iframe dynamically
    var mainContent = $("#mainContent");
    var contentInMainContent = '';
    $(".nav-sidebar-body .nav li a").each(function () {
        var val = $(this).attr("href").substr(1);
        var url = val.replace(/_/g, '/');
        console.log(url);
        contentInMainContent += '<div id="' + val + '" class="tab-pane fade">\n' +
            '                <iframe class="main-iframe" src="' + url + '" width="100%" frameborder="0"' +
            ' scrolling="auto"></iframe>\n' +
            '            </div>';
    });
    mainContent.html(contentInMainContent);
    mainContent.find("div").first().addClass("active in");

    // Automatically size the iframe height
    $(".main-iframe").on("load", function () {
        $(this).height($(window).height() - 55);
    });

    var toggleSidebar = $("#toggleSidebar");
    toggleSidebar.click(function () {
        // change icon
        toggleSidebar.toggleClass("glyphicon-arrow-left glyphicon-menu-hamburger");
        // hide/show sidebar
        $(".sidebar").toggle();
        // stretch/collapse main content
        mainContent.toggleClass("col-sm-offset-3")
            .toggleClass("col-md-offset-2")
            .toggleClass("col-sm-9")
            .toggleClass("col-md-10");
    });
});
