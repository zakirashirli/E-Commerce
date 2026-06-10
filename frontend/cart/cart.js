document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, initAuthUi, requireAuth } = window.ecommerceApi;
  initAuthUi();
  if (!requireAuth("../login/login.html")) return;

  const cartList = document.querySelector(".list");
  const subtotalElement = document.querySelector(".subtotalElement");
  const totalElement = document.querySelector(".totalElement");

  async function loadCart() {
    try {
      const cart = await apiRequest("/cart", { headers: authHeaders() });
      cartList.innerHTML = "";
      cart.items.forEach((item) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td><img width="70" src="${item.imageUrl || ""}" alt="${item.productName}"> <span>${item.productName}</span></td>
          <td>$${Number(item.price).toFixed(2)}</td>
          <td><input class="form-control quantity" min="1" style="width:70px" type="number" value="${item.quantity}"></td>
          <td>$${Number(item.subtotal).toFixed(2)}</td>
          <td><button class="btn btn-danger btn-sm remove-item">Remove</button></td>`;

        row.querySelector(".quantity").addEventListener("change", async (event) => {
          try {
            await apiRequest(`/cart/items/${item.itemId || item.id}`, {
              method: "PUT",
              headers: authHeaders(),
              body: JSON.stringify({ quantity: Number(event.target.value) }),
            });
            loadCart();
          } catch (error) {
            alert(error.message);
            loadCart();
          }
        });
        row.querySelector(".remove-item").addEventListener("click", async () => {
          await apiRequest(`/cart/items/${item.itemId || item.id}`, {
            method: "DELETE",
            headers: authHeaders(),
          });
          loadCart();
        });
        cartList.appendChild(row);
      });
      subtotalElement.textContent = `$${Number(cart.totalPrice).toFixed(2)}`;
      totalElement.textContent = `$${Number(cart.totalPrice).toFixed(2)}`;
    } catch (error) {
      cartList.innerHTML = `<tr><td class="text-danger">${error.message}</td></tr>`;
    }
  }

  document.querySelector(".checkoutBtn").addEventListener("click", () => {
    window.location.href = "../checkout/checkout.html";
  });
  loadCart();
});
