//验证邮箱格式是否正确
function isMailReg(mailvalue) {
    var mailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    return mailReg.test(mailvalue);
}

//验证是否包含特殊字符
function isStringReg(str) {
    var reg = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9!%$#,.]+$");
    return reg.test(str)
}

// 获取url参数的值
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if (r !== null) return decodeURI(r[2]);
    //if (r !== null) return decodeURI(unescape(r[2]));
    return null;
}

//修改url参数
function changeURLArg(url, arg, arg_val) {
    var pattern = arg + '=([^&]*)';
    var replaceText = arg + '=' + arg_val;
    if (url.match(pattern)) {
        var tmp = '/(' + arg + '=)([^&]*)/gi';
        tmp = url.replace(eval(tmp), replaceText);
        return tmp;
    } else {
        if (url.match('[\?]')) {
            return url + '&' + replaceText;
        } else {
            return url + '?' + replaceText;
        }
    }
}

//ajax post csrf解决方法
if (typeof ($) != "undefined") {
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajaxSetup({
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        });
    });
}

function calMyPagination(id, page, pageNum, url) {
    console.log(id + "\t" + page + "\t" + pageNum + "\t" + url);

    pageNum=parseInt(pageNum);
    page=parseInt(page);

    // ("#myPagination>li:eq(0)>a:eq(3)")
    $("#" + id + ">li:eq(3)>a:eq(0)").text(page);
    $("#" + id + ">li:eq(3)>a:eq(0)").attr('href', changeURLArg(url, "page", page));

    $("#" + id + ">li:eq(0)>a:eq(0)").attr('href', changeURLArg(url, "page", page - 1));

    $("#" + id + ">li:eq(1)>a:eq(0)").text(page - 2);
    $("#" + id + ">li:eq(1)>a:eq(0)").attr('href', changeURLArg(url, "page", page - 2));

    $("#" + id + ">li:eq(2)>a:eq(0)").text(page - 1);
    $("#" + id + ">li:eq(2)>a:eq(0)").attr('href', changeURLArg(url, "page", page - 1));

    $("#" + id + ">li:eq(4)>a:eq(0)").text(page + 1);
    $("#" + id + ">li:eq(4)>a:eq(0)").attr('href', changeURLArg(url, "page", page + 1));

    $("#" + id + ">li:eq(5)>a:eq(0)").text(page + 2);
    $("#" + id + ">li:eq(5)>a:eq(0)").attr('href', changeURLArg(url, "page", page + 2));

    $("#" + id + ">li:eq(6)>a:eq(0)").attr('href', changeURLArg(url, "page", page + 1));
    if (page === 1) {
        console.log("page 1");
        $("#" + id + ">li:eq(0)").addClass("disabled");
        $("#" + id + ">li:eq(1)").hide();
        $("#" + id + ">li:eq(2)").hide();
    }
    if (page === 2) {
        console.log("page 2");
        $("#" + id + ">li:eq(1)").hide();
    }
    if (page === pageNum) {
        console.log("page end");
        $("#" + id + ">li:eq(6)").addClass("disabled");
        $("#" + id + ">li:eq(5)").hide();
        $("#" + id + ">li:eq(4)").hide();
    }
    if (page === pageNum - 1) {
        console.log("page end-1");
        $("#" + id + ">li:eq(5)").hide();
    }
    $("#" + id).show();
}


// 获取cookie
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

//删除cookie
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval !== null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

// s设置cookie
function setCookie(name, value, time) {
    var strsec = getSec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

// setCookie("name","hayden","s20");
//这是有设定过期时间的使用示例：
//s20是代表20秒
//h是指小时，如12小时则是：h12
//d是天数，30天则：d30
function getSec(str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 === "s") {
        return str1 * 1000;
    } else if (str2 === "h") {
        return str1 * 60 * 60 * 1000;
    } else if (str2 === "d") {
        return str1 * 24 * 60 * 60 * 1000;
    }
}

