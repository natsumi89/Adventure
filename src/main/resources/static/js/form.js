$(function () {
    const url = "https://zipcoda.net/api?zipcode=";

    $("#search-address").on("click", function () {
        const zipcoda = $("#zipCode").val();

        if (zipcoda.length === 0) {
            alert("郵便番号を入力してください");
            return;
        }
        fetch(`${url}${zipcoda}`)
            .then(function (res) {
                if (res.ok === false) {
                    alert("不正な郵便番号です。入力内容を確認してください。");
                    return;
                }
                return res.json();
            })
            .then(function (data) {
                const address = `${data.items[0].pref}${data.items[0].address}`;
                $("#address").val(address);
            })
    });
})