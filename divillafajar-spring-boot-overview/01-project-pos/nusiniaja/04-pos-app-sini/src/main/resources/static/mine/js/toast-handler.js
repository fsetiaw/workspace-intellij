// toast-handler.js
document.addEventListener("DOMContentLoaded", function() {
  var toastElList = [].slice.call(document.querySelectorAll('.toast'))
  toastElList.forEach(function(toastEl) {
    // ambil data-delay dari attribute (default 3000 ms)
    var delay = toastEl.getAttribute("toast-delay") || 3000;
    var toast = new bootstrap.Toast(toastEl, { delay: delay })
    toast.show()
  })
});