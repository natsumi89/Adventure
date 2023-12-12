let handler = StripeCheckout.configure({
  key: 'pk_test_51NDOT0CcxxRiYOcnZUNon1HEJj1ai181kBFy8lKemibfaFbYMjnSKilBdc1ha6SvutSLAwj8azn4zDEy0ygzYSKZ00chcvpmtm',
  locale: 'ja',
  token: function(token) {
    // Handle the token (e.g., send it to your server)
    console.log('Token received:', token);
  }
});

function buttonClick() {
  let stripeButton = document.getElementById("stripe-button");
  let cashOnDelivery = document.getElementById("cashOnDelivery");
  let transfer = document.getElementById("transfer");

  if (cashOnDelivery.checked || transfer.checked) {
    stripeButton.style.display = "none";
  } else {
    stripeButton.style.display = "block";
  }
}

document.getElementById("cashOnDelivery").addEventListener("change", buttonClick);
document.getElementById("transfer").addEventListener("change", buttonClick);

document.getElementById("stripe-button").addEventListener("click", function(e) {
  // Fetch the total price directly from the input element
  let totalPrice = parseInt(document.getElementById("totalPrice").value);
  console.log('Total Price:', totalPrice);

  handler.open({
    name: 'tea-voyage',
    description: 'クレジット決済画面',
    zipCode: false,
    currency: "jpy",
    amount: totalPrice
  });
});

// Log a message when the script is loaded
console.log('Script loaded.');
