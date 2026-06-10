async function createAccount(event) {
  event.preventDefault();
  const { apiRequest, publicHeaders } = window.ecommerceApi;

  try {
    const data = await apiRequest("/auth/register", {
      method: "POST",
      headers: publicHeaders(),
      body: JSON.stringify({
        name: document.getElementById("name").value.trim(),
        surname: document.getElementById("surname").value.trim(),
        email: document.getElementById("email").value.trim(),
        username: document.getElementById("username").value.trim(),
        password: document.getElementById("password").value,
      }),
    });
    alert(data.message || "Account created. Verify your email before logging in.");
    window.location.href = "../login/login.html";
  } catch (error) {
    alert(error.message);
  }
}

document.addEventListener("DOMContentLoaded", () => window.ecommerceApi.initAuthUi());
