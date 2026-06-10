document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, initAuthUi, requireAdmin, requireAuth } = window.ecommerceApi;
  initAuthUi();
  if (!requireAuth("./login/login.html") || !requireAdmin("./home/home.html")) return;

  const form = document.querySelector(".form");
  const imageInput = document.querySelector(".imageInput");
  const imagePreview = document.querySelector(".imageInForm");
  const categoryInput = document.querySelector(".categoryInput");
  const editProductId = localStorage.getItem("editProductId");

  try {
    const categories = await apiRequest("/categories");
    categoryInput.innerHTML = '<option value="">Choose category</option>';
    categories.forEach((category) => {
      categoryInput.insertAdjacentHTML("beforeend", `<option value="${category.id}">${category.name}</option>`);
    });

    if (editProductId) {
      const product = await apiRequest(`/products/${editProductId}`);
      document.querySelector(".brandInput").value = product.brand || "";
      document.querySelector(".modelInput").value = product.name;
      categoryInput.value = product.categoryId;
      document.querySelector(".descriptionInput").value = product.description || "";
      document.querySelector(".priceInput").value = product.price;
      document.querySelector(".stockInput").value = product.stockQuantity;
      imageInput.value = product.imageUrl || "";
      imagePreview.src = product.imageUrl || "";
      document.querySelector(".title").textContent = "Edit Product";
    }
  } catch (error) {
    alert(error.message);
  }

  imageInput.addEventListener("input", () => {
    imagePreview.src = imageInput.value;
  });

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!form.checkValidity()) {
      form.classList.add("was-validated");
      return;
    }

    const body = {
      brand: document.querySelector(".brandInput").value.trim(),
      name: document.querySelector(".modelInput").value.trim(),
      categoryId: Number(categoryInput.value),
      description: document.querySelector(".descriptionInput").value.trim(),
      price: Number(document.querySelector(".priceInput").value),
      stockQuantity: Number(document.querySelector(".stockInput").value),
      imageUrl: imageInput.value.trim(),
    };

    try {
      await apiRequest(editProductId ? `/products/${editProductId}` : "/products", {
        method: editProductId ? "PUT" : "POST",
        headers: authHeaders(),
        body: JSON.stringify(body),
      });
      localStorage.removeItem("editProductId");
      window.location.href = "./userproducts.html";
    } catch (error) {
      alert(error.message);
    }
  });
});
