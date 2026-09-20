jQuery(function ($) {
    var categoryTreeBlock = $("#categoryTreeBlock");
    var treeSettingData = {
        key: {
            title: "description"
        },
        simpleData: {
            enable: true,
            pIdKey: "parentId"
        }
    };
    var categoryTreeSetting = {
        data: treeSettingData,
        callback: {
            onClick: onCategoryTreeNodeClick
        }
    };
    var categorySelectTreeSetting = {
        data: treeSettingData,
        callback: {
            onClick: onCategorySelectTreeNodeClick
        }
    };
    var categoryAddSelectTreeSetting = {
        data: treeSettingData,
        callback: {
            onClick: onCategoryAddSelectTreeNodeClick
        }
    };
    // All categories
    var allCategories = [];
    //Enabled Categories
    var enabledCategories = [];

    var inputCategoryId = $('#inputCategoryId');
    var inputName = $('#inputName');
    var inputDescription = $('#inputDescription');
    var inputParentId = $('#inputParentId');
    var inputLevel = $('#inputLevel');
    var inputParentName = $('#inputParentName');
    var originalLevel;

    var updateForm = $('#updateForm');
    var updateFormFieldset = updateForm.find('fieldset');

    var categoriesListTree;

    // Handle a category-tree node click
    function onCategoryTreeNodeClick(event, treeId, treeNode) {
        var parentNode = treeNode.getParentNode();
        if (parentNode === null) {
            updateFormFieldset.attr("disabled", "disabled");
        } else {
            updateFormFieldset.removeAttr("disabled");
        }
        inputCategoryId.val(treeNode.id);
        inputName.val(treeNode.name);
        inputDescription.html(treeNode.description);
        inputParentName.val(parentNode === null ? 'None' : parentNode.name);
        inputParentId.val(parentNode === null ? -1 : parentNode.id);
        inputLevel.val(treeNode.level);
        originalLevel = treeNode.level;
        if (treeNode.status) {
            updateForm.find('input[name="status"]')[0].checked = true;
        } else {
            updateForm.find('input[name="status"]')[1].checked = true;
        }
    }

    var hintModal = $('#hintModal');
    var hintContent = $('#hintContent');
    var hintModalHeader = hintModal.find('.modal-header');
    var hintModalBody = hintModal.find('.modal-body');

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

    // Handle a category-selection tree node click
    function onCategorySelectTreeNodeClick(event, treeId, treeNode) {
        if (treeNode.level >= originalLevel) {
            showHintModal('The new parent must be above the category's current level', false);
            return;
        }
        inputParentName.val(treeNode.name);
        inputParentId.val(treeNode.id);
        inputLevel.val(treeNode.level + 1);
        parentSelectModal.modal('hide');
    }

    function onCategoryAddSelectTreeNodeClick(event, treeId, treeNode) {
        if (treeNode.level === 3) {
            showHintModal('A level-three category cannot be selected as the parent', false);
            return;
        }
        inputAddParentName.val(treeNode.name);
        inputAddParentId.val(treeNode.id);
        inputAddLevel.val(treeNode.level + 1);
        parentSelectAddModal.modal('hide');
    }

    // Load all categories and initialize the category tree
    function getCategoriesAndInitTree() {
        $.get("/categories", function (data) {
            console.log(data);
            if (data.code === RESPONSE_CODE.SUCCESS) {
                allCategories = data.data;
                let rootCategory = {
                    id: 0,
                    name: "Root Category",
                    description: "Root Category",
                    level: 0,
                    status: true,
                    open: true
                };
                allCategories.push(rootCategory);
                console.log(allCategories);
                enabledCategories = [];
                for (var i in allCategories) {
                    var category = allCategories[i];
                    if (category.status) {
                        enabledCategories.push(category);
                    }
                }
                console.log(enabledCategories);
                // Initialize the tree with enabled categories
                categoriesListTree = $.fn.zTree.init(categoryTreeBlock, categoryTreeSetting, enabledCategories);
            } else {
                alert("error");
                // window.location.href = "/manage/errorPage";
            }
        }, DATA_TYPE.JSON);
    }

    var parentSelectModal = $('#parentSelectModal');
    var categorySelectTreeBlock = $('#categorySelectTreeBlock');

    function initBindSelectParentEvent() {
        $('#parentSelectBtn, #inputParentName').click(function () {
            // set parentSelectModal content
            $.fn.zTree.init(categorySelectTreeBlock, categorySelectTreeSetting, enabledCategories);
            // open modal
            parentSelectModal.modal();
        });
    }

    var parentSelectAddModal = $('#parentSelectAddModal');
    var categorySelectTreeAddBlock = $('#categorySelectTreeAddBlock');

    function initBindAddSelectParentEvent() {
        $('#parentSelectAddBtn, #inputAddParentName').click(function () {
            // set parentSelectModal content
            $.fn.zTree.init(categorySelectTreeAddBlock, categoryAddSelectTreeSetting, enabledCategories);
            // open modal
            parentSelectAddModal.modal();
        });
    }

    // Filter categories by enabled status
    function initFilterByDeleted() {
        $("#filterByDeletedMenu").find("li a").click(function () {
            var text = $(this).text();
            $("#filterByDeletedBtn").find(".text").text(text);
            var option = $(this).attr("class").trim();
            var categories;
            switch (option) {
                case "all-category":
                    categories = allCategories;
                    break;
                case "enabled-category":
                    categories = enabledCategories;
                    break;
                default:
            }
            categoriesListTree = $.fn.zTree.init(categoryTreeBlock, categoryTreeSetting, categories);
        });
    }

    function initUpdateListener() {
        $('#updateBtn').click(function () {
            // report validity
            var valid = document.querySelector("#updateForm").reportValidity();
            if (!valid) {
                return;
            }
            // send post request
            var category = JSON.stringify({
                name: inputName.val(),
                description: inputDescription.val(),
                parentId: inputParentId.val(),
                level: inputLevel.val(),
                status: updateForm.find('input[name="status"]:checked').val(),
            });
            console.log(category);
            $.ajax('/categories/' + inputCategoryId.val(), {
                data: category,
                contentType: 'application/json',
                type: 'PATCH',
                dataType: DATA_TYPE.JSON,
                success: function (data) {
                    if (data.code === RESPONSE_CODE.SUCCESS) {
                        reset();
                        showHintModal('Updated successfully', true);
                    } else {
                        showHintModal(data.data + ', Update failed', false);
                    }
                }
            });
        });
    }

    var inputAddName = $('#inputAddName');
    var inputAddDescription = $('#inputAddDescription');
    var inputAddParentId = $('#inputAddParentId');
    var inputAddLevel = $('#inputAddLevel');
    var inputAddParentName = $('#inputAddParentName');
    var addForm = $('#addForm');
    var addModal = $('#addModal');

    function initAddListener() {
        $('#addBtn').click(function () {
            // report validity
            var valid = document.querySelector("#addForm").reportValidity();
            if (!valid) {
                return;
            }
            // send post request
            var category = JSON.stringify({
                name: inputAddName.val(),
                description: inputAddDescription.val(),
                parentId: inputAddParentId.val(),
                level: inputAddLevel.val(),
                status: addForm.find('input[name="status"]:checked').val()
            });
            console.log(category);
            $.ajax('/categories', {
                data: category,
                contentType: 'application/json',
                type: 'POST',
                dataType: DATA_TYPE.JSON,
                success: function (data) {
                    // if data is the error page
                    if (data.code === RESPONSE_CODE.SUCCESS) {
                        reset();
                        showHintModal('Added successfully', true);
                        // hide add modal
                        addModal.modal('hide');
                    } else {
                        showHintModal(data.message + ', Add failed', false);
                    }
                }
            });
        });
    }

    function initDeleteListener() {
        $('#deleteBtn').click(() => {
            console.log(`delete id=${inputCategoryId.val()}`);
            $.ajax(`/categories/${inputCategoryId.val()}`,{
                type: 'DELETE',
                dataType: DATA_TYPE.JSON,
                success: function (data) {
                    // if data is the error page
                    if (data.code === RESPONSE_CODE.SUCCESS) {
                        reset();
                        showHintModal('Deleted successfully', true);
                    } else {
                        showHintModal(data.message + ', Delete failed', false);
                    }
                }
            });
        });
    }

    function disableUpdateForm() {
        updateFormFieldset.attr("disabled", "disabled");
    }

    // Reset the page
    function reset() {
        getCategoriesAndInitTree();
        // reset update form
        document.querySelector('#updateForm').reset();
        // reset add form
        document.querySelector('#addForm').reset();
        disableUpdateForm();
        $("#filterByDeletedBtn").find(".text").text('Enabled Categories');
    }

    function initRefreshListener() {
        $("#refreshBtn").click(function () {
            reset();
        });
    }

    function initAddCategoryListener() {
        $("#addCategoryBtn").click(function () {
            var selectedCategories = categoriesListTree.getSelectedNodes();
            if(selectedCategories.length === 0) {
                return;
            }
            var parent = selectedCategories[0];
            if(parent.level === 3) {
               return;
            }
            inputAddParentId.val(parent.id);
            inputAddParentName.val(parent.name);
            inputAddLevel.val(parent.level + 1);
        });
    }

    function init() {
        // disable form by default
        disableUpdateForm();
        getCategoriesAndInitTree();
        initFilterByDeleted();
        initBindSelectParentEvent();
        initUpdateListener();
        initAddListener();
        initRefreshListener();
        initBindAddSelectParentEvent();
        initAddCategoryListener();
        initDeleteListener();
    }

    $(document).ready(function () {
        init();
    });
});
