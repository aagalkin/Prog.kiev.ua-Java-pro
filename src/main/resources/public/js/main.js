function show_container() {
    document.getElementById("bg-popup").style.display='block';
    document.getElementById("container").style.display='block';
}

function hide_container() {
    document.getElementById("bg-popup").style.display='none';
    document.getElementById("container").style.display='none';
}

function remove_product_from_container(elem) {
    var product_id = elem.previousElementSibling.getAttribute("value");
    var client_id = document.getElementById("client_id").getAttribute("value");
    var container_count = document.getElementById("cont_count");
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", "/container/remove", true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.onreadystatechange = function() {
        if(this.readyState == XMLHttpRequest.DONE && this.status == 200) {
            var summ = elem.nextElementSibling.nextElementSibling.nextElementSibling.firstElementChild.nextElementSibling.firstElementChild.firstElementChild;
            var total_summ = document.getElementById("total_summ").firstElementChild;
            total_summ.innerText = parseInt(total_summ.innerText) - parseInt(summ.innerHTML);
            elem.parentElement.remove();
            container_count.innerText = xmlhttp.responseText;
        }
    };
    var data = "client_id="+client_id+"&product_id="+product_id;
    xmlhttp.send(data);
}

function calc_count(button) {
    var field;
    if(button.getAttribute("id") === "plus") {
        field = button.previousElementSibling.firstElementChild;
        if (parseInt(field.getAttribute("value")) > 998) return;
    } else {
        field = button.nextElementSibling.firstElementChild;
        if (parseInt(field.getAttribute("value")) < 2) return;
    }

    var price = button.parentElement.previousElementSibling;
    var summ = button.parentElement.parentElement.parentElement.nextElementSibling.firstElementChild.nextElementSibling.firstElementChild.firstElementChild;
    var total_summ = document.getElementById("total_summ").firstElementChild;

    var digit; button.getAttribute("id") === "plus" ? digit = 1 : digit = -1;
    var new_field_value = parseInt(field.getAttribute("value")) + digit;

    var new_field = document.createElement("input");
    new_field.setAttribute("type", "number");
    new_field.setAttribute("value", new_field_value);
    new_field.setAttribute("name", "count");
    new_field.setAttribute("style", "width: 70px");
    new_field.setAttribute("onclick", field.getAttribute("onclick"));

    var place = field.parentElement;
    total_summ.innerHTML = parseInt(total_summ.innerHTML) - parseInt(summ.innerHTML);
    field.remove();
    place.appendChild(new_field);
    summ.innerHTML = parseInt(new_field.getAttribute("value")) * parseInt(price.innerHTML);
    total_summ.innerHTML = parseInt(total_summ.innerHTML) + parseInt(summ.innerHTML);
}

function calc_count_field_open(field) {
    var calc_bg = field.parentElement.nextElementSibling.nextElementSibling;
    var calc_popup = field.parentElement.previousElementSibling.previousElementSibling.previousElementSibling;
    var part1 = field.parentElement.previousElementSibling.previousElementSibling;
    var part2 = field.parentElement.previousElementSibling;
    var part3 = field.parentElement;
    var part4 = field.parentElement.nextElementSibling;
    part1.style.display = 'none';
    part2.style.display = 'none';
    part3.style.display = 'none';
    part4.style.display = 'none';
    calc_bg.style.display = 'block';
    calc_popup.style.display = 'block';
    calc_popup.firstElementChild.firstElementChild.focus();
}

function cacl_count_field_close(button) {
    var calc_bg = button.parentElement.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
    var calc_popup = button.parentElement;
    var part1 = button.parentElement.nextElementSibling;
    var part2 = button.parentElement.nextElementSibling.nextElementSibling;
    var part3 = button.parentElement.nextElementSibling.nextElementSibling.nextElementSibling;
    var part4 = button.parentElement.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
    calc_bg.style.display = 'none';
    calc_popup.style.display = 'none';

    part1.style.display = 'block';
    part2.style.display = 'block';
    part3.style.display = 'block';
    part4.style.display = 'block';
}

function calc_confirm_custom_edit(confirm_button) {
    var field = confirm_button.parentElement.nextElementSibling.nextElementSibling.nextElementSibling.firstElementChild;

    var price = confirm_button.parentElement.parentElement.previousElementSibling;
    var summ = confirm_button.parentElement.parentElement.parentElement.parentElement.nextElementSibling.firstElementChild.nextElementSibling.firstElementChild.firstElementChild;
    var total_summ = document.getElementById("total_summ").firstElementChild;
    var input = confirm_button.previousElementSibling.firstElementChild;
    if (isNaN(parseInt(input.value))) {
        alert("Введите число");
        return;
    }
    var new_field_value = parseInt(input.value);
    if (new_field_value < 1 || new_field_value > 999) {
        alert("Число должно быть не меньше 1 и не больше 999");
        return;
    }

    var new_field = document.createElement("input");
    new_field.setAttribute("type", "number");
    new_field.setAttribute("value", new_field_value);
    new_field.setAttribute("name", "count");
    new_field.setAttribute("style", "width: 70px");
    new_field.setAttribute("onclick", field.getAttribute("onclick"));

    var place = confirm_button.parentElement.nextElementSibling.nextElementSibling.nextElementSibling;
    total_summ.innerHTML = parseInt(total_summ.innerHTML) - parseInt(summ.innerHTML);
    field.remove();
    place.appendChild(new_field);
    summ.innerHTML = parseInt(new_field.getAttribute("value")) * parseInt(price.innerHTML);
    total_summ.innerHTML = parseInt(total_summ.innerHTML) + parseInt(summ.innerHTML);
    cacl_count_field_close(confirm_button);
}

function f() {
    var c = document.getElementsByTagName("td");
    c.forEach()
}