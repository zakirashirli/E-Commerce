document.addEventListener("DOMContentLoaded", async () => {
  const { apiRequest, authHeaders, clearSession, initAuthUi, requireAuth, saveSession } = window.ecommerceApi;
  initAuthUi();
  if (!requireAuth("./login/login.html")) return;

  try {
    const user = await apiRequest("/auth/me", { headers: authHeaders() });
    saveSession(user);
    const productsLink = document.querySelector('a[href="./userproducts.html"]');
    if (productsLink && user.role !== "ADMIN") productsLink.style.display = "none";
    ["name", "surname", "email", "username"].forEach((field) => {
      const element = document.getElementById(field);
      if (element) element.textContent = user[field] || "";
    });
  } catch (error) {
    alert(error.message);
    clearSession();
    window.location.href = "./login/login.html";
  }
});
