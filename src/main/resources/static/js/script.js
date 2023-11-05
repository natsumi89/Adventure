    function amountChange(cartId, newAmount) {


        fetch(`/update-cart-quantity?cartId=${cartId}&newAmount=${newAmount}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        }).then(response => {
            if(response.ok) {
                location.reload();
            } else {
                alert('数量の更新に失敗しました。');
            }
        });
    }

