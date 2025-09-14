// toast-handler.js
document.addEventListener("DOMContentLoaded", function() {
  var toastElList = [].slice.call(document.querySelectorAll('.toast'))
  toastElList.forEach(function(toastEl) {
    // ambil data-delay dari attribute (default 3000 ms)
    var delay = toastEl.getAttribute("toast-delay") || 3000;
    var toast = new bootstrap.Toast(toastEl, { delay: delay })
    toast.show()
  });

  // --- 2. Logout confirmation toast ---
    const logoutLink = document.getElementById('logoutLink');
    const logoutLink2 = document.getElementById('logoutLink2');
    const logoutForm = document.getElementById('logoutForm');

    [logoutLink, logoutLink2].forEach(link => {
      if (link) { // pastikan element ada
        link.addEventListener('click', function(e) {
          e.preventDefault();

          // buat toast logout dinamis
          const toastContainer = document.createElement('div');
          toastContainer.className = 'toast-container position-fixed top-50 start-50 translate-middle p-3';
          toastContainer.style.zIndex = 1100;

          toastContainer.innerHTML = `
            <div class="toast align-items-center text-bg-warning border-0" role="alert" aria-live="assertive" aria-atomic="true">
              <div class="d-flex">
                <div class="toast-body">
                  🔒 Are you sure you want to logout?
                </div>
                <button type="button" class="btn btn-sm btn-outline-secondary me-2" id="confirm-logout">Yes</button>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" id="cancel-logout"></button>
              </div>
            </div>
          `;

          document.body.appendChild(toastContainer);

          const toastEl = toastContainer.querySelector('.toast');
          const toast = new bootstrap.Toast(toastEl, { delay: 0, autohide: false });
          toast.show();

          toastContainer.querySelector('#confirm-logout').addEventListener('click', function() {
            logoutForm.submit();
          });
          toastContainer.querySelector('#cancel-logout').addEventListener('click', function() {
            toast.hide();
            toastEl.addEventListener('hidden.bs.toast', function() {
              toastContainer.remove();
            });
          });
        });
      }
    });
});