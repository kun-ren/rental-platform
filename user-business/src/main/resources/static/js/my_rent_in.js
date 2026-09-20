$(document).ready(function () {
    //Deletion confirmation area
    var deleteConfirmBlock = $("#deleteConfirm");
    var singleCheckBoxes = $(".single-checkbox");
    var selectAllCheckBox = $("#selectAllCheckBox");
    var multiDeleteBtn = $("#multiDeleteBtn");
    var dropdownMenu1 = $("#dropdownMenu1");

    //Show the deletion confirmation dialog
    $(".btn-single-delete").click(function () {
        deleteConfirmBlock.slideDown();
    });

    multiDeleteBtn.click(function () {
        if (singleCheckBoxes.filter(":checked").length === 0) {
            $(this).popover('show');
            setTimeout(function () {
                multiDeleteBtn.popover('hide');
            }, 1000);
        } else {
            deleteConfirmBlock.slideDown();
        }
    });

    //Hide the deletion confirmation dialog
    deleteConfirmBlock.find("button").click(function () {
        deleteConfirmBlock.hide(500);
    });

    //Handle deletion confirmation
    deleteConfirmBlock.find(".confirm").click(function () {
        // delete the selected item
    });

    var pageNumBtns = $(".pagination .number");

    //Update the highlighted page number
    pageNumBtns.click(function () {
        pageNumBtns.removeClass("active");
        $(this).addClass("active");
    });

    //Go to the previous page
    $(".pagination .previous").click(function () {
        var activePageObject = $(".pagination .active");
        var pageNum = parseInt(activePageObject.text());
        activePageObject.removeClass("active");
        if (pageNum === parseInt(pageNumBtns.first().text())) {
            pageNumBtns.last().addClass("active");
            return;
        }
        pageNumBtns.each(function () {
            if ($(this).text() === pageNum - 1 + "") {
                $(this).addClass("active");
                return false;
            }
        });
    });

    //Go to the next page
    $(".pagination .next").click(function () {
        var activePageObject = $(".pagination .active");
        var pageNum = parseInt(activePageObject.text());
        activePageObject.removeClass("active");
        if (pageNum === parseInt(pageNumBtns.last().text())) {
            pageNumBtns.first().addClass("active");
            return;
        }
        pageNumBtns.each(function () {
            if ($(this).text() === pageNum + 1 + "") {
                $(this).addClass("active");
                return false;
            }
        });
    });

    //Table-header labels
    var thsText = [];
    $("table").find("th").each(function () {
        thsText.push($(this).text());
    });

    //Show one table row's details
    $(".detail").parents("td").click(function () {
        $(this).find("span").toggleClass("glyphicon-plus glyphicon-minus");

        if ($(this).parents("tr").next().hasClass("detail-block")) {
            $(this).parents("tr").next().fadeOut(function () {
                $(this).remove();
            });
            return;
        }
        var tdsText = [];
        $(this).parents("tr").children().each(function () {
            tdsText.push($(this).text());
        });

        var detailContent = '';
        for (var i = 2; i < thsText.length - 1; i++) {
            detailContent += '<dt>' + thsText[i] + ':</dt><dd>' + tdsText[i] + '</dd>';
        }
        var colNum = $(this).parents("tr").children().length;
        var detail = '<tr class="detail-block info">\n' +
            '                <td colspan="' + colNum + '">' +
            '<dl class="dl-horizontal">\n' +
            detailContent +
            '</dl>' +
            '</td>\n' +
            '         </tr>';
        $(this).parents("tr").after(detail);
        var newRow = $(this).parents("tr").next();
        newRow.hide();
        newRow.fadeIn("slow");
    });

    //set table show animate
    var tableBlock = $("#tableBlock");
    tableBlock.hide();
    tableBlock.slideDown(1000);

    // checkbox select all
    selectAllCheckBox.click(function () {
        if ($(this).is(':checked')) {
            singleCheckBoxes.prop("checked", true);
        } else {
            singleCheckBoxes.prop("checked", false);
        }
    });
    singleCheckBoxes.click(function () {
        if ($(this).is(':checked')) {
            if (singleCheckBoxes.length === singleCheckBoxes.filter(":checked").length) {
                selectAllCheckBox.prop("checked", true);
            }
        } else {
            selectAllCheckBox.prop("checked", false);
        }
    });

    $("#refreshBtn").click(function () {
        window.location.reload(true);
    });

    // init dropdown menu content
    var dropdownMenuContent = '';
    for (var i in thsText) {
        if (thsText[i].trim().length !== 0) {
            dropdownMenuContent += '<li>\n' +
                '                <label>\n' +
                '                    <input type="checkbox" checked="checked">' + thsText[i] + '\n' +
                '                </label>\n' +
                '            </li>';
        }
    }
    dropdownMenu1.html(dropdownMenuContent);

    // toggle a column of table
    dropdownMenu1.find("input").click(function () {
        var index = thsText.indexOf($(this).parents("label").text().trim());
        // toggle th
        tableBlock.find("th").eq(index).toggle();
        // toggle td
        tableBlock.find("tr").each(function () {
            $(this).find("td").eq(index).toggle();
        });
    });

    // --------------------------

    const hintModal = $('#hintModal');
    const hintContent = $('#hintContent');
    const hintModalHeader = hintModal.find('.modal-header');
    const hintModalBody = hintModal.find('.modal-body');
    // Show the notification dialog
    function showHintModal(message, success) {
        if (success) {
            hintModalHeader.attr('class', 'modal-header bg-success');
            hintModalBody.attr('class', 'modal-body text-success');
        } else {
            hintModalHeader.attr('class', 'modal-header bg-warning');
            hintModalBody.attr('class', 'modal-body text-warning');
        }
        hintContent.text(message);
        hintModal.modal();
    }

    $('.cancelApplyBtn').click(function () {
        let itemId = $(this).parents('tr').find('input[name="inputItemId"]').val();
        $.ajax(`/items/${itemId}/cancel-apply`, {
            type: 'POST',
            dataType: DATA_TYPE.JSON,
            success: function (data) {
                if (data.code === RESPONSE_CODE.SUCCESS) {
                    window.location.reload(true);
                } else {
                    showHintModal(data.message + ', Failed to cancel the application', false);
                }
            }
        });
    });

    //Rating
    let itemId = 0;
    $(".ratingBtn").click(function () {
        console.log("Click event");
        itemId = $(this).parents('tr').find('input[name="inputItemId"]').val();
    });
    $("#ratingSubmitBtn").click(function () {
        console.log("Send request");
         $.ajax(`/rating/${itemId}`,{
             data:{
                 score: $('#inputScore').val()
             },
             type: 'POST',
             dataType: DATA_TYPE.JSON,
             success: function (data) {
                 if(data.code === RESPONSE_CODE.SUCCESS) {
                     showHintModal('Rating submitted successfully',false)
                 } else {
                     showHintModal(data.message + ',Failed to submit the rating',false);
                 }
             }
         })
    });



    function replaceContent(content) {
        document.open();
        document.write(content);
        document.close();
    }

    $('.payBtn').click(function () {
        let itemId = $(this).parents('tr').find('input[name="inputItemId"]').val();
        $.ajax(`/items/${itemId}/pay`, {
            type: 'POST',
            dataType: DATA_TYPE.JSON,
            success: function (data) {
                if (data.code === RESPONSE_CODE.SUCCESS) {
                    replaceContent(data.data);
                } else {
                    showHintModal(data.message + ', Payment failed', false);
                }
            }
        });
    });
});
