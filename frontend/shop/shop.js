document.addEventListener("DOMContentLoaded", () => {
  const { apiRequest, initAuthUi } = window.ecommerceApi;
  const container = document.getElementById("products-container");
  const categoryList = document.getElementById("categoryList");
  const searchInput = document.getElementById("searchInput");
  let selectedCategoryId = null;

  initAuthUi();

  const sortDropdown = document.createElement("select");
  sortDropdown.className = "form-select mt-3";
  sortDropdown.innerHTML = `
    <option value="name,asc">Name</option>
    <option value="price,asc">Price: Low to High</option>
    <option value="price,desc">Price: High to Low</option>
  `;
  categoryList.insertAdjacentElement("afterend", sortDropdown);

  function renderProducts(products) {
    container.innerHTML = "";
    if (!products.length) {
      container.innerHTML = '<p class="text-muted">No products found.</p>';
      return;
    }

    products.forEach((product) => {
      const col = document.createElement("div");
      col.className = "col-md-4 mb-4";
      col.innerHTML = `
        <div class="card h-100">
          ${product.imageUrl
            ? `<img src="${product.imageUrl}" class="card-img-top" alt="${product.name}">`
            : '<div class="card-img-top bg-light text-center py-5">No Image</div>'}
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${product.name}</h5>
            <p class="card-text">${product.description || "No description"}</p>
            <p><strong>${product.brand || ""}</strong> ${product.categoryName || ""}</p>
            <p class="card-text"><strong>$${Number(product.price).toFixed(2)}</strong></p>
            <a class="btn btn-dark mt-auto" href="../productpage.html?id=${product.id}">View product</a>
          </div>
        </div>`;
      container.appendChild(col);
    });
  }

  async function loadProducts() {
    const [sortBy, direction] = sortDropdown.value.split(",");
    const params = new URLSearchParams({ page: "0", size: "100", sortBy, direction });
    const name = searchInput.value.trim();
    if (name) params.set("name", name);
    if (selectedCategoryId) params.set("categoryId", selectedCategoryId);

    try {
      const data = await apiRequest(`/products/filter?${params}`);
      renderProducts(data.content || data);
    } catch (error) {
      container.innerHTML = `<p class="text-danger">${error.message}</p>`;
    }
  }

  async function loadCategories() {
    const categories = await apiRequest("/categories");
    categories.forEach((category) => {
      const item = document.createElement("li");
      item.className = "list-group-item";
      item.textContent = category.name;
      item.addEventListener("click", () => {
        document.querySelectorAll("#categoryList .list-group-item").forEach((el) => el.classList.remove("active"));
        item.classList.add("active");
        selectedCategoryId = category.id;
        loadProducts();
      });
      categoryList.appendChild(item);
    });
  }

  categoryList.querySelector(".list-group-item").addEventListener("click", (event) => {
    document.querySelectorAll("#categoryList .list-group-item").forEach((el) => el.classList.remove("active"));
    event.currentTarget.classList.add("active");
    selectedCategoryId = null;
    loadProducts();
  });
  searchInput.addEventListener("input", loadProducts);
  sortDropdown.addEventListener("change", loadProducts);

  loadCategories().catch(console.error);
  loadProducts();
});
