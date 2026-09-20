var functions = {};
functions.oFormplace = document.getElementById("formplace");
functions.oErrorinfo = document.getElementById("error-info");
functions.oErrorbox = document.getElementById("error-box");
functions.aInputs = functions.oFormplace.getElementsByTagName("input");

// Check whether a string is an email address
functions.checkEmailFormat = function (strEmail) {
    //Define the email regular expression
    var emailRegex = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!emailRegex.test(strEmail)) {
        console.log("invalid email");
        return false;
    }
    return true;
};

//Show the success icon
functions.showRightSpan = function (obj) {
    var oSpan = obj.parentNode.parentNode.getElementsByTagName("span")[0];
    if (oSpan) {
        oSpan.className = "checkspan checkright";
    }
};

//Show the error icon
functions.showWrongSpan = function (obj) {
    var oSpan = obj.parentNode.parentNode.getElementsByTagName("span")[0];
    if (oSpan) {
        oSpan.className = "checkspan checkwrong";
    }
};

functions.hideCheckSpan = function (obj) {
    var oSpan = obj.parentNode.parentNode.getElementsByTagName("span")[0];
    if (oSpan) {
        oSpan.className = "";
    }
};

functions.changeOutline = function (obj) {
    obj.style['border'] = "1px solid #ff7c87";
    obj.style['box-shadow'] = "0px 0px 20px #ffb3b2";
};

functions.resetOutline = function (obj) {
    obj.style['border'] = "0px";
    obj.style['box-shadow'] = "none";
};

// Set the message
functions.setMessage = function (str) {
    functions.oErrorinfo.innerHTML = str;
};

//Show the message
functions.showMessageBox = function (obj) {
    var tempLeft = 0;
    var tempTop = 0;

    while (obj != undefined) {//Equivalent to assigning obj = obj.offsetParent in the loop
        tempLeft += obj.offsetLeft; //Accumulate the parent container's left offset
        tempTop += obj.offsetTop; //Accumulate the parent container's top offset
        obj = obj.offsetParent;
    }
    functions.oErrorbox.style['left'] = tempLeft + 320 + "px";
    functions.oErrorbox.style['top'] = tempTop + 2 + "px";
    functions.oErrorbox.style['display'] = "block";
    functions.oErrorinfo.style['display'] = "block";
};

//Hide the message
functions.hideMessageBox = function () {
    functions.oErrorbox.style['display'] = "none";
    functions.oErrorinfo.style['display'] = "none";
};

functions.focusInputBack = function (obj, str) {
    obj.parentNode.style['background-position'] = str;

};

functions.blurInputBack = function (obj, str) {
    obj.parentNode.style['background-position'] = str;
};

// Show the success icon and hide the message
functions.showRightSpanAndHideMessage = function (obj) {
    functions.showRightSpan(obj);
    functions.resetOutline(obj);
    functions.hideMessageBox(obj);
};

// Show the success icon and message
functions.showRightSpanAndMessage = function (obj, str) {
    functions.showRightSpan(obj);
    functions.changeOutline(obj);
    functions.setMessage(str);
    functions.showMessageBox(obj);
};

// Show the error icon and message
functions.showWrongSpanAndMessage = function (obj, str) {
    functions.showWrongSpan(obj);
    functions.changeOutline(obj);
    functions.setMessage(str);
    functions.showMessageBox(obj);
};
