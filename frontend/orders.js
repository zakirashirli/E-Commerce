document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, requireAuth } = window.ecommerceApi;
  if (!requireAuth("./login/login.html")) return;
  const section = document.getElementById("ordersSection");
  const isAdmin = localStorage.getItem("role") === "ADMIN";

  try {
    const data = await apiRequest(isAdmin ? "/orders?page=0&size=100&sortBy=id&direction=desc" : "/orders/my", {
      headers: authHeaders(),
    });
    const orders = data.content || data;
    if (!orders.length) {
      section.innerHTML = '<div class="alert alert-info">No orders found.</div>';
      return;
    }

    section.innerHTML = `
      <table class="table table-bordered bg-white">
        <thead class="table-dark"><tr><th>ID</th><th>Date</th><th>Status</th><th>Total</th><th>Items</th>${isAdmin ? "<th>Update</th>" : ""}</tr></thead>
        <tbody>${orders.map((order) => `
          <tr>
            <td>${order.id}</td><td>${new Date(order.createdAt).toLocaleString()}</td>
            <td>${order.status}</td><td>$${Number(order.totalAmount).toFixed(2)}</td>
            <td>${order.items.map((item) => `${item.productName} x${item.quantity}`).join("<br>")}</td>
            ${isAdmin ? `<td><select class="form-select status-select" data-id="${order.id}">
              ${["PENDING", "PAID", "SHIPPED", "CANCELLED"].map((status) => `<option ${status === order.status ? "selected" : ""}>${status}</option>`).join("")}
            </select></td>` : ""}
          </tr>`).join("")}</tbody>
      </table>`;

    document.querySelectorAll(".status-select").forEach((select) => {
      select.addEventListener("change", async () => {
        try {
          await apiRequest(`/orders/${select.dataset.id}/status`, {
            method: "PUT",
            headers: authHeaders(),
            body: JSON.stringify({ status: select.value }),
          });
        } catch (error) {
          alert(error.message);
        }
      });
    });
  } catch (error) {
    section.innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
  }
});
