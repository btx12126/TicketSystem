// Page: Add ticket  ->  POST /tickets/add-ticket

document.getElementById("ticket-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const form = event.target;
  try {
    const price = form.elements.price.value.replace(",", ".");
    const ticket = await request(
      withParams("/tickets/add-ticket", { price: price, category: form.elements.category.value }),
      { method: "POST" }
    );
    showMessage("Ticket added with ID " + ticket.id + ".", "ok", { href: "index.html", label: "View available tickets" });
    form.reset();
  } catch (e) {
    showMessage(e.message, "error");
  }
});
