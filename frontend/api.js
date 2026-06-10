const API_BASE_URL = "http://localhost:8080/api";

function getToken() {
  return localStorage.getItem("token");
}

function publicHeaders() {
  return { "Content-Type": "application/json" };
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${getToken()}`,
  };
}

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, options);
  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = data?.message || data?.error || `Request failed (${response.status})`;
    throw new Error(message);
  }

  return data;
}

function saveSession(data) {
  ["token", "userId", "name", "surname", "username", "email", "role"].forEach((key) => {
    if (data[key] !== undefined && data[key] !== null) {
      localStorage.setItem(key, data[key]);
    }
  });
}

function clearSession() {
  ["token", "userId", "name", "surname", "username", "email", "role", "editProductId"].forEach(
    (key) => localStorage.removeItem(key)
  );
}

function requireAuth(loginPath) {
  if (!getToken()) {
    window.location.href = loginPath;
    return false;
  }
  return true;
}

function requireAdmin(homePath) {
  if (localStorage.getItem("role") !== "ADMIN") {
    alert("This page is available to administrators only.");
    window.location.href = homePath;
    return false;
  }
  return true;
}

function initAuthUi() {
  const usernameDisplay = document.getElementById("usernameDisplay");
  const loginBtn = document.getElementById("loginBtn");
  const logoutBtn = document.getElementById("logoutBtn");
  const username = localStorage.getItem("username");

  if (usernameDisplay) usernameDisplay.textContent = username || "";
  if (loginBtn) loginBtn.style.display = getToken() ? "none" : "inline-block";
  if (logoutBtn) {
    logoutBtn.style.display = getToken() ? "inline-block" : "none";
    logoutBtn.addEventListener("click", (event) => {
      event.preventDefault();
      clearSession();
      window.location.reload();
    });
  }
}

window.ecommerceApi = {
  API_BASE_URL,
  apiRequest,
  authHeaders,
  clearSession,
  getToken,
  initAuthUi,
  publicHeaders,
  requireAdmin,
  requireAuth,
  saveSession,
};
