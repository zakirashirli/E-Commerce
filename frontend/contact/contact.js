document.addEventListener("DOMContentLoaded", () => {
  const { apiRequest, initAuthUi, publicHeaders } = window.ecommerceApi;
  initAuthUi();

  const form = document.querySelector(".card form");
  const inputs = form.querySelectorAll("input");
  const message = form.querySelector("textarea");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    try {
      await apiRequest("/contact", {
        method: "POST",
        headers: publicHeaders(),
        body: JSON.stringify({
          name: inputs[0].value.trim(),
          email: inputs[1].value.trim(),
          phone: inputs[2].value.trim(),
          subject: "Website contact message",
          message: message.value.trim(),
        }),
      });
      alert("Message sent successfully.");
      form.reset();
    } catch (error) {
      alert(error.message);
    }
  });
});
