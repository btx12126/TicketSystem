// Page: Buy ticket  ->  POST /order/buy

const buyForm = document.getElementById("buy-form");

// Pre-fill the form from the address, e.g. buy.html?ticketId=3&clientId=1
const params = new URLSearchParams(window.location.search);
if (params.get("ticketId")) buyForm.elements.ticketId.value = params.get("ticketId");
if (params.get("clientId")) buyForm.elements.clientId.value = params.get("clientId");

buyForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const purchase = await request(
      withParams("/order/buy", {
        clientId: buyForm.elements.clientId.value,
        ticketId: buyForm.elements.ticketId.value
      }),
      { method: "POST" }
    );
    showMessage("Purchase saved (purchase ID: " + purchase.id + ").", "ok", { href: "index.html", label: "Back to available tickets" });
    buyForm.elements.ticketId.value = "";
  } catch (e) {
    showMessage(e.message, "error");
  }
});
