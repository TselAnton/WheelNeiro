let lastColor;

getLastColorFunc = function() {
    const request = new XMLHttpRequest();
    request.responseType = "json";
    request.open('GET', ' http://127.0.0.1:8080/result/last');
    request.send();

    alert(request.status)

    if (request.status === 200) {
        // let color = JSON.parse(request.responseText);
        alert(request.responseText);
    }
};


while (true) {
    getLastColorFunc.call();
}
