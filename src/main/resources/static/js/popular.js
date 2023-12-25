"use strict"

    document.addEventListener("DOMContentLoaded", function () {
        // スライドのインターバル（ミリ秒）
        var interval = 5000; // 5秒ごとにスライド

        // スライダー要素の取得
        var slider = document.querySelector('.top-product-slider');

        // 一つの商品コンテナの幅を取得
        var productWidth = document.querySelector('.product-container').offsetWidth;

        // スライドの実行
        function slide() {
            // スライダーを左に1つ分の商品コンテナ幅だけ移動
            slider.style.transform = 'translateX(-' + productWidth + 'px)';

            // 一定時間経過後に元の位置に戻す
            setTimeout(function () {
                slider.style.transform = 'translateX(0)';
            }, 1000); // 1秒後に元に戻す（この時間はスライド時間より大きくする）
        }

        // インターバルごとにスライドを実行
        setInterval(slide, interval);
    });
