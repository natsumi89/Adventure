    $(document).ready(function () {
        // 画面内遷移用のリンクがクリックされた時の処理
        $(".scroll-link").on("click", function (event) {
            event.preventDefault();

            // 対象のセクションの位置を取得
            var target = $(this.hash);
            var targetOffset = target.offset().top;

            // ヘッダーの高さを取得
            var headerHeight = $("header").outerHeight();

            // スクロール位置を設定（ヘッダーの高さを考慮）
            $("html, body").animate({
                scrollTop: targetOffset - headerHeight
            }, 600);
        });
    });