// Page: Available tickets  ->  GET /tickets/check-available

const ticketsBody = document.getElementById("tickets");
const ticketsEmpty = document.getElementById("tickets-empty");

async function loadTickets() {
  try {
    const tickets = await request("/tickets/check-available");
    ticketsBody.innerHTML = "";
    ticketsEmpty.hidden = tickets.length > 0;

    for (const ticket of tickets) {
      const row = document.createElement("tr");

      const id = document.createElement("td");
      id.textContent = ticket.id;

      const category = document.createElement("td");
      category.textContent = ticket.category;

      const price = document.createElement("td");
      price.textContent = formatPrice(ticket.price);

      const action = document.createElement("td");
      const buy = document.createElement("a");
      buy.className = "button small";
      buy.href = "buy.html?ticketId=" + encodeURIComponent(ticket.id);
      buy.textContent = "Buy";
      action.appendChild(buy);

      row.append(id, category, price, action);
      ticketsBody.appendChild(row);
    }
  } catch (e) {
    showMessage(e.message, "error");
  }
}

document.getElementById("refresh").addEventListener("click", loadTickets);
loadTickets();
