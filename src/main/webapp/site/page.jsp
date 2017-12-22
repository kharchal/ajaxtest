<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <style>
        a {
            text-decoration: underline;

        }
        a:link {
            color: blue;
        }
        a:visited {
            color: purple;
        }
        a:hover {
            color: blue;
            cursor: pointer;
        }
        a:active {
            color: red;
        }
        fieldset fieldset{
            height: 50px;
        }
        .paginator {
            font-size: small;
            color: blue;
        }

        .lock {
            /*overflow: hidden;*/
        }
        .shim {
            position: fixed;
            bottom: 0; left: 0; top: 0; right: 0;
            overflow: auto;
            background: rgba(0,0,0,0.5);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#7F000000,endColorstr=#7F000000); /* IE6–IE8 */
            zoom: 1;
        }
        .error {
            color: red;
            font-size: smaller;
        }
    </style>

</head>
<body class="lock">
    <div id="main">

        <h2><a href="page.jsp">The page</a> <a onclick="newdata();">generate new data</a> </h2>
    <form onsubmit="process(); return false;">
        <fieldset>
            <legend>Search values</legend>
            <table>
                <colgroup>
                    <col width="25%"/>
                    <col width="25%"/>
                    <col width="10%"/>
                    <col width="40%"/>
                </colgroup>
                <tr>
                    <td colspan="3">
                        <%--page: --%>
                        <input type="hidden" id="page" name="page" value="0" size="3"/>
                        <%--perpage: --%>
                        <input type="hidden" id="perpage" name="perpage" value="5" size="3" readonly/>
                        <%--pages: --%>
                        <input type="hidden" id="pages" name="pages" value="0" size="3" readonly/>
                        search: <span id="search_clear" style="color: red; font-weight: bold;">X</span>
                        <input id="search" name="search" onsubmit="foo();"/>
                        <select id="search_selector">
                            <option value="name">by name</option>
                            <option value="login">by login</option>
                            <option value="address">by address</option>
                            <option value="email">by email</option>
                        </select>
                        &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;
                        <select id="perpage_selector">
                            <option value="2">by 2</option>
                            <option value="5" selected>by 5</option>
                            <option value="10">by 10</option>
                            <option value="15">by 15</option>
                            <option value="50">by 50</option>
                        </select>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;<button id="magic">Filter.</button>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fieldset>
                            <legend>
                                Period <input type="checkbox" id="period_on" value="false"/>
                            </legend>
                            from: <input type="date" id="date_from" name="date_from" readonly/>
                            to: <input type="date" id="date_to" name="date_to" readonly/>
                        </fieldset>
                    </td>
                    <td>
                        <fieldset>
                            <legend>
                                Age <input type="checkbox" id="age_on" value="0"/>
                            </legend>
                            from: <input type="number" id="age_from" name="age_from" value="0" readonly/>
                            to: <input type="number" id="age_to" name="age_to" value="100" readonly/>
                        </fieldset>
                    </td>
                    <td>
                        <fieldset>
                            <legend>
                                Active<input type="checkbox" id="active_on" value="0"/>
                            </legend>
                            active: <input type="checkbox" name="active" id="active" disabled/>
                        </fieldset>
                    </td>
                </tr>
            </table>
        </fieldset>
    <br>
    <%--sort: --%>
    <input type="hidden" id="sort" name="sort_col" value="id"/>
    <%--dir: --%>
    <input type="hidden" id="sort_dir" name="sort_dir" value="up"/>


        <%--<input type="submit" onclick="magic();return false;"/>--%>
    </form>
        <div id="wait"><h3>Receiving data...</h3><img src="loading_small.gif" alt="waiting for data..."/></div>
        <div id="results">
    <div style="color:green;">Results found: <span id="results_count">0</span></div>
    <div class="paginator">pagination</div>
    <div id="point">Here is the place!</div>
    <div class="paginator">pagination</div>
        </div>
    </div>
<div class="shim">
    <div id="edit" style="border: solid 3px blue; background-color: white; height: 400px; width: 600px; position: absolute;
    top: 50%;
    left: 50%;
    z-index: 2;
    margin: -200px 0 0 -300px;
    display: none;
    /*top: 250px; right: 500px;*/
    ">
        <h3 align="center">Edit form</h3>
        <%--<form>--%>
            <table cellpadding="3" style="padding-left: 20px;">
                <tr>
                    <td><label for="edit_id">ID:</label></td><td><input name="id" id="edit_id"/></td><td></td>
                </tr>
                <tr>
                    <td><label for="edit_name">NAME:</label></td><td><input name="name" id="edit_name"/></td><td><span id="edit_name_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_login">LOGIN:</label></td><td><input name="login" id="edit_login"/></td><td><span id="edit_login_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_address">ADDRESS:</label></td><td><input name="address" id="edit_address"/></td><td><span id="edit_address_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_email">EMAIL:</label></td><td><input name="email" id="edit_email"/></td><td><span id="edit_email_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_active">ACTIVE:</label></td><td><input type="checkbox" name="active" id="edit_active"/></td><td><span id="edit_active_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_date">DATE:</label></td><td><input name="date" id="edit_date"  />
                    <br><input id="edit_date_toread" type="hidden">
                </td><td><span id="edit_date_error" class="error"></span></td>
                </tr>
                <tr>
                    <td><label for="edit_age">AGE:</label></td><td><input name="age" id="edit_age"/></td><td><span id="edit_age_error" class="error"></span></td>
                </tr>
                <tr>
                    <td colspan="2" align="right"><button id="send_btn">Save</button></td>
                    <td><button id="close_btn">Close</button></td>
                </tr>
            </table>
        <%--</form>--%>
    </div>
</div>
</body>
<script>
    function process() {
//        alert("enter!!!????");
        magic();
    }
    function close() {
//        alert("close!");
        $('#edit').hide();
        $(".shim").hide();
//        return false;
    }
    function reset() {
//        alert("reset");
        $(".shim").hide();
        $("#edit").hide();
        $("#wait").hide();
        $("#results").hide();
        $("#date_from").datepicker({showWeek: true, firstDay: 1, changeMonth: true, changeYear: true});
        $("#date_to").datepicker({showWeek: true, firstDay: 1, changeMonth: true, changeYear: true});
        $("#date_from").datepicker("option", "dateFormat", "yyyy-mm-dd");
        $("#date_to").datepicker("option", "dateFormat", "yyyy-mm-dd");
        $("#date_from").val("1700-01-01");
        $("#date_to").val("2700-12-31");
        $("#edit_date").datepicker({showWeek: true, firstDay: 1, changeMonth: true, changeYear: true, altField: "#edit_date_toread", altFormat: "yy-mm-dd"});
        $("#edit_date").datepicker("option", "dateFormat", "yy-mm-dd");
        $("#period_on").prop("checked", false);
        $("#active_on").prop('checked', false);
        $("#age_on").prop("checked", false);
        $("#active").attr("disabled", true);
        $("#date_from").attr("readonly", true);
        $("#date_to").attr("readonly", true);
        $("#age_from").attr("readonly", true);
        $("#age_to").attr("readonly", true);
    }
    function send() {
        var data = {
            cmd: 'edit.save',
            id: $("#edit_id").val(),
            name: $("#edit_name").val(),
            login: $("#edit_login").val(),
            address: $("#edit_address").val(),
            email: $("#edit_email").val(),
            active: $("#edit_active").is(":checked"),
            age: $("#edit_age").val(),
            date: $("#edit_date_toread").val()
        };
        $.post("ajax/", data, function (data_rcvd) {
            data_rcvd = data_rcvd.trim();
            if (data_rcvd == 'ok') {
                alert("save is done!");
                close();
                magic();
            } else {}
            var data = JSON.parse(data_rcvd);

//            alert("errors :{" +
//            "\r\n#edit_age_error: "+data.age+
//            "\r\n#edit_age_name: "+data.name+
//            "\n#edit_age_login: "+data.login+
//            "\n#edit_age_address: "+data.address+
//            "\n#edit_age_active: "+data.active+
//            "\n#edit_age_email: "+data.email+
//            "\n#edit_age_date: "+data.date)+
//                "\n}"

            $("#edit_age_error").text(data.age == null ? "" : data.age);
            $("#edit_name_error").text(data.name == null ? "" : data.name);
            $("#edit_login_error").text(data.login == null ? "" : data.login);
            $("#edit_address_error").text(data.address == null ? "" : data.address);
            $("#edit_active_error").text(data.active == null ? "" : data.active);
            $("#edit_email_error").text(data.email == null ? "" : data.email);
            $("#edit_date_error").text(data.date == null ? "" : data.date);

        });
    }
    $(document).ready(function () {
        reset();
        $("#search_clear").click(function () {
            $("#search").val("");
        });
        $("#close_btn").click(function () {
           close();
        });
        $("#send_btn").click(function () {
            send();
        });
        $("#magic").click(function () {
            magic();
        });
        $("#perpage_selector").change(function () {
            v = $("#perpage_selector option:selected").val();
            $("#perpage").val(v);
            magic();
        });
        $("#page").change(function () {
            magic();
        });
        $("#active_on").change(function () {
            if ($("#active_on").is(":checked")) {
                $("#active").attr("disabled", false);
            } else {
                $("#active").attr("disabled", true);
            }
        });
        $("#period_on").change(function () {
            if ($("#period_on").is(":checked")) {
                $("#date_from").attr("readonly", false);
                $("#date_to").attr("readonly", false);
            } else {
                $("#date_from").attr("readonly", true);
                $("#date_to").attr("readonly", true);
            }
        });
        $("#age_on").change(function () {
            if ($("#age_on").is(":checked")) {
                $("#age_from").attr("readonly", false);
                $("#age_to").attr("readonly", false);
            } else {
                $("#age_from").attr("readonly", true);
                $("#age_to").attr("readonly", true);
            }
        });
    });

    function magic() {
        $("#results").hide();
        url = "ajax/";//?page=" + $("#page").val() + "&perpage=" + $("#perpage").val() + "&search=" + $("#search").val();
//        alert(url);
        data = {
                cmd: 'get.table',
                page: $("#page").val(),
                perpage: $("#perpage").val(),
                search: $("#search").val(),
                where_search: $("#search_selector option:selected").val(),
                date_from: $("#date_from").val(),
                date_to: $("#date_to").val(),
                period_on: $("#period_on").is(":checked"),
                active: $("#active").is(':checked'),
                active_on: $("#active_on").is(':checked'),
                age_on: $("#age_on").is(':checked'),
                age_from: $("#age_from").val(),
                age_to: $("#age_to").val(),
                sort: $("#sort").val(),
                sort_dir: $("#sort_dir").val()
        };
//        alert(data.active);
        $("#wait").show();
        $.get(url, data, function (data) {
            var obj = JSON.parse(data.trim());
//            alert(obj.pages);
            $("#point").html(obj.table);
            $("#pages").val(obj.pages);
            $(".paginator").html(obj.paginator);
            $("#results_count").html(obj.count);
            $("#page").val(obj.page);
            $("#wait").hide();
            $("#results").show();
        });

    }
    function setPage(n) {
        $("#page").val(n);
        magic();
    }
    function setSort(th) {
        str = $(th).text();

        ind = str.indexOf(" ");
        $("#sort").val(ind < 0 ? str : str.substr(0, ind));
//        alert(str + ": " + ind + ": " + $("#sort").val());

        prev = $("#sort_dir").val();
        var next;
        if (prev == "up") {
            next = "down";
        } else {
            next = "up";
        }
        $("#sort_dir").val(next);
        magic();
    }
    function edit(id) {
        $.get("ajax/", {cmd: 'get.edit', id: id}, function (data) {
//            alert(data);
            data = JSON.parse(data);
            $("#edit_id").val(data.id);
            $("#edit_name").val(data.name);
            $("#edit_login").val(data.login);
            $("#edit_address").val(data.address);
            $("#edit_email").val(data.email);
            $("#edit_active").prop("checked", data.active);
            $("#edit_age").val(data.age);
            $("#edit_date").val(data.dateString);

            $("#edit_age_error").text('');
            $("#edit_name_error").text('');
            $("#edit_login_error").text('');
            $("#edit_address_error").text('');
            $("#edit_active_error").text('');
            $("#edit_email_error").text('');
            $("#edit_date_error").text('');
//
//  $("#edit_date_time").val(data.date.time);
            $("#main").prop("disabled", true);

            $(".shim").show();
            $("#edit").show();
        });
    }
    function newdata() {
        var data = {
            cmd: 'reboot'
        }
        $.get("ajax/", data, function () {
            magic();
        });
    }
</script>
</html>
