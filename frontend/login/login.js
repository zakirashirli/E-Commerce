async function login(event) {
  event.preventDefault();
  const { apiRequest, publicHeaders, saveSession } = window.ecommerceApi;

  try {
    const data = await apiRequest("/auth/login", {
      method: "POST",
      headers: publicHeaders(),
      body: JSON.stringify({
        username: document.getElementById("username").value.trim(),
        password: document.getElementById("password").value,
      }),
    });
    saveSession(data);
    window.location.href = "../home/home.html";
  } catch (error) {
    alert(error.message);
  }
}

document.addEventListener("DOMContentLoaded", () => window.ecommerceApi.initAuthUi());
