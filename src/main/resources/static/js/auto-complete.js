$(document).ready(function () {
    $("#top-name").on("input", function () {
        const url = 'http://localhost:8080/auto-complete';
        const name = $('#top-name').val();

        if (name.length < 2) {
            // 入力が2文字未満の場合はオートコンプリートを表示しない
            return;
        }

        const params = { name: name };

        fetch(`${url}?${new URLSearchParams(params)}`)
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Network response was not ok");
                }
                return res.json();
            })
            .then(function (data) {
                // オートコンプリートの処理を実装
                $("#top-name").autocomplete({
                    source: data, // サーバーからのデータを渡す
                    minLength: 2
                });
            })
            .catch(function (error) {
                console.error("Error:", error);
            });
    });
});
