// Backend address. Change it if your Spring application runs on another port.
const API_URL = "http://localhost:8080";

const messageBox = document.getElementById("message");

// Shows a green (ok) or red (error) message at the top of the page.
// Optional `link` = { href, label } adds a link at the end of the message.
function showMessage(text, type, link) {
  messageBox.textContent = text;
  messageBox.className = "message " + type;
  if (link) {
    messageBox.append(" ");
    const a = document.createElement("a");
    a.href = link.href;
    a.textContent = link.label;
    messageBox.appendChild(a);
  }
  messageBox.hidden = false;
}

// Sends a request to the API and returns the parsed JSON.
// Throws an Error with a readable description when something goes wrong.
async function request(path, options = {}) {
  let response;
  try {
    response = await fetch(API_URL + path, options);
  } catch (e) {
    throw new Error("Cannot reach the server (" + API_URL + "). Is the backend running and is CORS enabled?");
  }

  const text = await response.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch (e) { data = text; }

  if (!response.ok) {
    const detail = data && (data.detail || data.message || data.error);
    throw new Error("Error " + response.status + (detail ? ": " + detail : ""));
  }
  return data;
}

// Builds a path with query parameters, e.g. "/order/buy?clientId=1&ticketId=2".
function withParams(path, params) {
  return path + "?" + new URLSearchParams(params).toString();
}

function formatPrice(value) {
  return Number(value).toFixed(2) + " PLN";
}
