document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, initAuthUi, requireAuth } = window.ecommerceApi;
  initAuthUi();
  if (!requireAuth("../login/login.html")) return;

  const form = document.querySelector("form.form");
  const subtotalElement = document.querySelector(".subtotal");
  const totalElement = document.querySelector(".total");

  try {
    const cart = await apiRequest("/cart", { headers: authHeaders() });
    subtotalElement.textContent = `$${Number(cart.totalPrice).toFixed(2)}`;
    totalElement.textContent = `$${Number(cart.totalPrice).toFixed(2)}`;
  } catch (error) {
    alert(error.message);
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!form.checkValidity()) {
      form.classList.add("was-validated");
      return;
    }

    const values = new FormData(form);
    try {
      const order = await apiRequest("/orders/checkout", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
          fullName: `${values.get("name")} ${values.get("surname")}`.trim(),
          phone: values.get("tel"),
          email: values.get("email"),
          address: values.get("address"),
          city: values.get("city"),
          country: values.get("state"),
          postalCode: values.get("zip"),
          paymentMethod: "CARD",
        }),
      });
      alert(`Order #${order.id} created successfully.`);
      window.location.href = "../orders.html";
    } catch (error) {
      alert(error.message);
    }
  });
});
