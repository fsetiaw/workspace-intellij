// 🔹 Ambil dari global bean siapin di controller
/* model.addAttribute("toastShortTimeout", globals.get("toastShortTimeout"));
*/
//const toastShortTimeout = 5000;
//const toastShortTimeout = [[${globals['toastShortTimeout']}]];
//console.log(toastShortTimeout);

//console.log("Short timeout:", toastShortTimeout);
// ---------- Utility helpers ----------
function showToastHtml(html, duration = 1500, bg = "#198754") {
    Toastify({
      text: html,
      duration: duration,
      close: true,
      gravity: "bottom",
      position: "center",
      backgroundColor: bg
    }).showToast();
}

function showToastText(text, duration = 1500, bg = "#198754", warna = "#6c757d") {
    console.log("showToastText di click");
    showToastHtml(`<strong style="color:${warna}; text-transform:uppercase;">${text}</strong>`, duration, bg);
}