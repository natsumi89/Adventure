function amountChange(id, value, productId) {
    console.log(`Changing amount for productId: ${productId}, value: ${value}`);
    fetch(`/change-order/amount?productId=${productId}&value=${value}`, {
        method: 'GET'
    }).then(response => {
        if (!response.ok) {
            throw new Error("Failed to update the quantity.");
        }
        return response.json();
    }).then(data => {
        console.log('Received newTotalPrice:', data.newTotalPrice);
        document.querySelector('.shopping-cart-total-price p').textContent = '合計: ' + data.newTotalPrice + '円';
        document.getElementById(id).value = value;
    }).catch(error => {
        console.error("Error updating quantity:", error);
    });
}
