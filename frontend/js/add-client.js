// Page: Add client  ->  POST /client/add-client

document.getElementById("client-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const form = event.target;
  try {
    const client = await request(
      withParams("/client/add-client", { name: form.elements.name.value, surname: form.elements.surname.value }),
      { method: "POST" }
    );
    showMessage(
      "Client added with ID " + client.id + ".",
      "ok",
      { href: "buy.html?clientId=" + encodeURIComponent(client.id), label: "Buy a ticket as this client" }
    );
    form.reset();
  } catch (e) {
    showMessage(e.message, "error");
  }
});
