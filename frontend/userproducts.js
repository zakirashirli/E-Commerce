document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, initAuthUi, requireAdmin, requireAuth } = window.ecommerceApi;
  initAuthUi();
  if (!requireAuth("./login/login.html") || !requireAdmin("./home/home.html")) return;

  const tbody = document.querySelector(".tbody");

  async function loadProducts() {
    try {
      const data = await apiRequest("/products?page=0&size=100&sortBy=id&direction=asc");
      tbody.innerHTML = "";
      data.content.forEach((product) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${product.id}</td>
          <td>${product.brand || ""}</td>
          <td>${product.name}</td>
          <td>${product.categoryName || ""}</td>
          <td><img src="${product.imageUrl || ""}" alt="${product.name}" width="50"></td>
          <td>$${Number(product.price).toFixed(2)}</td>
          <td>${product.stockQuantity}</td>
          <td>
            <button class="btn btn-warning btn-sm edit-btn">Edit</button>
            <button class="btn btn-danger btn-sm delete-btn">Delete</button>
          </td>`;
        row.querySelector(".edit-btn").addEventListener("click", () => {
          localStorage.setItem("editProductId", product.id);
          window.location.href = "./newproduct.html";
        });
        row.querySelector(".delete-btn").addEventListener("click", async () => {
          if (!confirm(`Delete ${product.name}?`)) return;
          try {
            await apiRequest(`/products/${product.id}`, { method: "DELETE", headers: authHeaders() });
            loadProducts();
          } catch (error) {
            alert(error.message);
          }
        });
        tbody.appendChild(row);
      });
    } catch (error) {
      tbody.innerHTML = `<tr><td colspan="8" class="text-danger">${error.message}</td></tr>`;
    }
  }

  document.getElementById("newProductBtn").addEventListener("click", () => localStorage.removeItem("editProductId"));
  loadProducts();
});
