let handler = StripeCheckout.configure({
  key: 'pk_test_51NDOT0CcxxRiYOcnZUNon1HEJj1ai181kBFy8lKemibfaFbYMjnSKilBdc1ha6SvutSLAwj8azn4zDEy0ygzYSKZ00chcvpmtm',
  locale: 'ja',
  token: function(token) {
  }
});

function buttonClick() {
  let creditButton = document.getElementById("credit");
  let cashButton = document.getElementById("cashOnDelivery");
  let transferButton = document.getElementById("transfer");
  let bankInfo = document.getElementById("bank-info");
  let stripeButton = document.getElementById("stripe-button");

  if (transferButton.checked) {
    bankInfo.style.display = "none";
    stripeButton.style.display = "block";
  } else if (cashButton.checked) {
    bankInfo.style.display = "none";
    stripeButton.style.display = "block";
  } else if (creditButton.checked) {
    bankInfo.style.display = "none";
    stripeButton.style.display = "block";
  } else {
    bankInfo.style.display = "none";
    stripeButton.style.display = "none";
  }
};

document.getElementById('stripe-button').addEventListener('click', function(e) {
  let totalPrice = parseInt(document.getElementById("totalPrice").innerText);
  handler.open({
    name: 'adventure',
    description: 'クレジット決済画面',
    zipCode: false,
    currency: "jpy",
    amount: totalPrice
  });
});