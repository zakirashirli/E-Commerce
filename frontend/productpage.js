document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, getToken, initAuthUi } = window.ecommerceApi;
  initAuthUi();

  const productId = new URLSearchParams(window.location.search).get("id");
  if (!productId) {
    document.body.innerHTML = "<h2 class='text-center text-danger'>Product ID is missing</h2>";
    return;
  }

  try {
    const product = await apiRequest(`/products/${productId}`);
    document.getElementById("product-image").src = product.imageUrl || "";
    document.getElementById("product-title").textContent = product.name;
    document.getElementById("product-price").textContent = `$${Number(product.price).toFixed(2)}`;
    document.getElementById("product-rating").textContent = product.brand || product.categoryName || "";
    document.getElementById("product-description").textContent = product.description || "";

    document.getElementById("add-to-cart").addEventListener("click", async () => {
      if (!getToken()) {
        window.location.href = "./login/login.html";
        return;
      }
      try {
        await apiRequest("/cart/items", {
          method: "POST",
          headers: authHeaders(),
          body: JSON.stringify({ productId: product.id, quantity: 1 }),
        });
        alert("Product added to cart.");
      } catch (error) {
        alert(error.message);
      }
    });
  } catch (error) {
    document.body.innerHTML = `<h2 class="text-center text-danger">${error.message}</h2>`;
  }
});
