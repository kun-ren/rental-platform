var usernameValid = false;
var usernameNotExist = true;
var passwordValid = false;
var confirmedPasswordValid = false;
var emailValid = false;
var emailNotExist = true;
var emailCaptchaValid = false;

function checkEmailSendButton() {
    document.getElementById("codebutton").disabled = !emailValid;
}

function checkRegisterButton() {
    console.log({
        "usernameValid": usernameValid,
        "usernameNotExist": usernameNotExist,
        "passwordValid": passwordValid,
        "confirmedPasswordValid": confirmedPasswordValid,
        "emailValid": emailValid,
        "emailNotExist": emailNotExist,
        "emailCaptchaValid": emailCaptchaValid
    });
    document.getElementById("submitbutton").disabled = !(usernameValid && passwordValid &&
        confirmedPasswordValid && emailValid && emailCaptchaValid && usernameNotExist && emailNotExist);
}

window.onload = function () {
    var usernameObject = document.getElementById("username");
    var passwordObject = document.getElementById("password");
    var confirmedPasswordObject = document.getElementById("confirmedPassword");
    var emailObject = document.getElementById("email");
    var emailCaptchaObject = document.getElementById("emailCaptcha");

    var firstPassword = "";

    functions.checkUsername = function () {
        usernameObject.onfocus = function () {
            functions.focusInputBack(this, "0px -55px");
            functions.resetOutline(this);
            functions.hideCheckSpan(this);
        };

        usernameObject.onkeyup = function () {
            functions.blurInputBack(this, "0px 0px");
            var thisValue = this.value;
            //Required and cannot contain spaces
            if (thisValue == null || thisValue.replace(/\s/g, "").length === 0 || thisValue.indexOf(" ") !== -1) {
                functions.showWrongSpanAndMessage(this, "This field is required");
                usernameValid = false;
                // 5-15 characters
            } else if (thisValue.length < 5 || thisValue.length > 15) {
                functions.showWrongSpanAndMessage(this, "Length must be 5-15 characters. Do not include names, identity numbers, bank card numbers, or other private information");
                usernameValid = false;
                // Chinese or English letters, digits, and underscores only
            } else if (/^[0-9a-zA-Z\u4e00-\u9fa5_]{5,15}$/.test(thisValue) === false) {
                functions.showWrongSpanAndMessage(this, "The username may contain Chinese or English letters, digits, and underscores");
                usernameValid = false;
            } else {
                usernameValid = true;
            }
            if (usernameValid && usernameNotExist) {
                functions.showRightSpanAndHideMessage(this);
            }
            checkRegisterButton();
        }
    };

    functions.checkPassword = function () {
        passwordObject.onfocus = function () {
            functions.focusInputBack(this, "-325px -55px");
            functions.resetOutline(this);
            functions.hideCheckSpan(this);
        };

        passwordObject.onkeyup = passwordObject.onblur = function () {
            functions.focusInputBack(this, "-325px 0px");
            var thisValue = this.value;
            //Cannot consist only of spaces; leading and trailing spaces are allowed
            if (thisValue == null || thisValue.replace(/\s/g, "").length === 0) {
                functions.showWrongSpanAndMessage(this, "This field is required");
                passwordValid = false;
                //6-16 characters
            } else if (thisValue.length < 6 || thisValue.length > 16) {
                functions.showWrongSpanAndMessage(this, "Length must be 6-16 characters");
                passwordValid = false;
                //Letters, digits, and ASCII punctuation only
            } else if (/^[0-9a-zA-Z\u4e00-\u9fa5_]{6,16}$/.test(thisValue) === false) {
                functions.showWrongSpanAndMessage(this, "The password may contain letters, digits, and punctuation");
                passwordValid = false;
            } else if (thisValue !== functions.aInputs[2].value) {
                firstPassword = thisValue;
                functions.showWrongSpanAndMessage(confirmedPasswordObject, "The passwords do not match");
                functions.showRightSpan(this);
                functions.resetOutline(this);
                passwordValid = false;
            } else {
                firstPassword = thisValue;
                functions.showRightSpanAndHideMessage(this);
                functions.showRightSpanAndHideMessage(confirmedPasswordObject);
                passwordValid = true;
            }
            checkRegisterButton();
        }
    };

    functions.checkConfirmedPassword = function () {
        confirmedPasswordObject.onfocus = function () {
            functions.focusInputBack(this, "-325px -55px");
            functions.resetOutline(this);
            functions.hideCheckSpan(this);
        };

        confirmedPasswordObject.onkeyup = confirmedPasswordObject.onblur = function () {
            functions.focusInputBack(this, "-325px 0px");
            var thisValue = this.value;
            if (thisValue == null || thisValue.replace(/\s/g, "").length === 0) {
                functions.showWrongSpanAndMessage(this, "This field is required");
                confirmedPasswordValid = false;
            } else if (firstPassword !== thisValue) {
                functions.showWrongSpanAndMessage(this, "The passwords do not match");
                confirmedPasswordValid = false;
            } else {
                functions.showRightSpanAndHideMessage(this);
                confirmedPasswordValid = true;
                passwordValid = true;
            }
            checkRegisterButton();
        }
    };

    functions.checkEmail = function () {
        emailObject.onfocus = function () {
            functions.focusInputBack(this, "-650px -55px");
            functions.resetOutline(this);
            functions.hideCheckSpan(this);
        };

        emailObject.onkeyup = emailObject.onblur = function () {
            functions.focusInputBack(this, "-650px 0px");
            var thisValue = this.value;
            // Required
            if (thisValue == null || thisValue.replace(/\s/g, "").length === 0) {
                functions.showWrongSpanAndMessage(this, "This field is required");
                emailValid = false;
                // Email length cannot exceed 50 characters
            } else if (thisValue.length > 50) {
                functions.showWrongSpanAndMessage(this, "The email address cannot exceed 50 characters");
                emailValid = false;
                // Validate the email format
            } else if (functions.checkEmailFormat(thisValue) === false) {
                functions.showWrongSpanAndMessage(this, "The email address format is invalid");
                emailValid = false;
                // Must not duplicate an existing email address
            } else {
                emailValid = true;
            }
            if (emailValid && emailNotExist) {
                functions.showRightSpanAndHideMessage(this);
            }
            checkEmailSendButton();
        }
    };

    functions.checkEmailCaptcha = function () {
        emailCaptchaObject.onfocus = function () {
            functions.focusInputBack(this, "-975px -55px");
            functions.resetOutline(this);
            functions.hideCheckSpan(this);
        };

        emailCaptchaObject.onkeyup = emailCaptchaObject.onblur = function () {
            functions.focusInputBack(this, "-975px 0px");
            var thisValue = this.value;
            //Required
            if (thisValue == null || thisValue.replace(/\s/g, "").length === 0) {
                functions.showWrongSpanAndMessage(this, "This field is required");
                emailCaptchaValid = false;
            } else if (thisValue.length !== 6) {
                functions.showWrongSpanAndMessage(this, "The verification code must contain six characters");
                emailCaptchaValid = false;
            } else if (/^[0-9a-zA-Z]+$/.test(thisValue) === false) {
                functions.showWrongSpanAndMessage(this, "The verification code may contain only letters or digits");
                emailCaptchaValid = false;
            } else {
                functions.showRightSpanAndHideMessage(this);
                emailCaptchaValid = true;
            }
            checkRegisterButton();
        }
    };

    functions.checkUsername();
    functions.checkPassword();
    functions.checkConfirmedPassword();
    functions.checkEmail();
    functions.checkEmailCaptcha();

    //----------------------------------------

    // Check whether the username exists
    usernameObject.onblur = function () {
        //check before send
        if (!usernameValid) {
            return;
        }
        $.ajax(`/users/${usernameObject.value}/check-exists`, {
            type: 'POST',
            dataType: DATA_TYPE.JSON,
            success: function (data) {
                if (data.code === RESPONSE_CODE.SUCCESS) {
                    usernameNotExist = true;
                } else {
                    usernameNotExist = false;
                    functions.showWrongSpanAndMessage(usernameObject, data.message);
                }
                if (usernameValid && usernameNotExist) {
                    functions.showRightSpanAndHideMessage(usernameObject);
                }
                checkRegisterButton();
            }
        });
    };

    // Send the email verification code
    let codebutton = document.getElementById("codebutton");
    codebutton.onclick = () => {
        // check valid
        if (!emailValid) {
            return;
        }
        $.ajax(`/emails/${emailObject.value}/send-captcha`, {
            type: 'POST',
            dataType: DATA_TYPE.JSON,
            success: function (data) {
                console.log(data);
                if (data.code === RESPONSE_CODE.SUCCESS) {
                    emailNotExist = true;
                    functions.showRightSpanAndMessage(emailObject, "The verification code was sent. Check your email and enter it here");
                } else {
                    emailNotExist = false;
                    functions.showWrongSpanAndMessage(emailObject, data.message);
                }
                checkRegisterButton();
            }
        });
    };

    // Register
    $('#submitbutton').click(function () {
        $.ajax('/users', {
            data: JSON.stringify({
                username: usernameObject.value,
                password: passwordObject.value,
                confirmedPassword: confirmedPasswordObject.value,
                sex: $('form').find('input[name="sex"]:checked').val(),
                email: emailObject.value,
                emailCaptcha: emailCaptchaObject.value,
                role: $('form').find('input[name="role"]:checked').val(),
            }),
            contentType: 'application/json',
            dataType: DATA_TYPE.JSON,
            type: 'POST',
            success: function (data) {
                if (data.code === RESPONSE_CODE.SUCCESS) {
                    window.location.href = "/login";
                } else {
                    let field = data.field;
                    if (field === null) {
                        field = "username";
                    }
                    if (field === "sex") {
                        field = "sex-secret";
                    }
                    if (field === "role") {
                        field = "role-lessee";
                    }
                    functions.showWrongSpanAndMessage(document.getElementById(field), data.message);
                }
            }
        });
    });
};
