var script = document.createElement('script');
script.type = 'text/javascript';
script.text = function autoLogin(userNameValue,passWordValue,userNameID,passWordID,userName,passWord) {
    if (userNameID && passWordID) {
        var field = document.getElementById(userNameID);
        field.value=userNameValue;
        var field = document.getElementById(passWordID);
        field.value=passWordValue;
    }
    else if (userName && passWord) {
        var field = document.getElementsByName(userName)[0];
        field.value=userNameValue;
        var field = document.getElementsByName(passWord)[0];
        field.value=passWordValue;
    }
    else {
        var elements = document.getElementsByTagName('*');
        var userNameIndex = 0;
        var passWordIndex = 0;
        for (var ii = 0; ii < elements.length; ii++) {
            if (elements[ii].type == 'password') {
                passWordIndex = ii;
                for (var jj = ii; jj > 0; jj--) {
                    if (elements[jj].type == 'text') {
                        userNameIndex = jj;
                    }
                }
            }
        }
        if (userNameIndex == 0 || passWordIndex == 0) {
            return 0;
        }
        var field = elements[userNameIndex];
        field.value=userNameValue;
        var field = elements[passWordIndex];
        field.value=passWordValue;
    }
    document.forms[0].submit();
    return 1;
};
document.getElementsByTagName('head')[0].appendChild(script);
